package com.deeptech.deeptech_test.Service;

import com.deeptech.deeptech_test.BaseAsset.ResponseDataList;
import com.deeptech.deeptech_test.Dto.CutiCreateRequestDto;
import com.deeptech.deeptech_test.Dto.CutiResponseDto;
import com.deeptech.deeptech_test.Dto.CutiSearchRequestDto;
import com.deeptech.deeptech_test.Exception.CutiAlreadyExitsException;
import com.deeptech.deeptech_test.Exception.DataNotFoundException;
import com.deeptech.deeptech_test.Model.Cuti;
import com.deeptech.deeptech_test.Model.Pegawai;
import com.deeptech.deeptech_test.Repository.CutiRepository;
import com.deeptech.deeptech_test.Util.MetadataMapperUtil;
import com.deeptech.deeptech_test.Util.ModelMapperUtil;
import com.deeptech.deeptech_test.Util.SearchingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CutiService {

    private final CutiRepository cutiRepository;
    private final PegawaiService pegawaiService;

    public CutiResponseDto createCuti(CutiCreateRequestDto cutiCreateRequestDto) throws Exception {
        Pegawai pegawai = pegawaiService.getById(cutiCreateRequestDto.getPegawaiId());
        Cuti cuti = new Cuti();
        cuti.setAlasan(cutiCreateRequestDto.getAlasan());
        cuti.setTanggalMulai(cutiCreateRequestDto.getTanggalMulai());
        cuti.setTanggalSelesai(cutiCreateRequestDto.getTanggalSelesai());

        if (cutiCreateRequestDto.getTanggalSelesai().isBefore(cutiCreateRequestDto.getTanggalMulai()))
            throw new CutiAlreadyExitsException("Tanggal selesai harus setelah tanggal mulai");

        validateCuti(cutiCreateRequestDto.getPegawaiId(), cutiCreateRequestDto);

        cuti.setPegawai(pegawai);
        Cuti savedCuti = cutiRepository.save(cuti);
        return ModelMapperUtil.convert(savedCuti,CutiResponseDto.class);
    }

    public CutiResponseDto updateCutiTerakhir(CutiCreateRequestDto cutiCreateRequestDto) throws Exception {
        Long pegawaiId = cutiCreateRequestDto.getPegawaiId();

        Cuti lastCuti = cutiRepository.findTopByPegawaiIdOrderByTanggalMulaiDesc(pegawaiId)
                .orElseThrow(() -> new CutiAlreadyExitsException("Belum ada data cuti yang bisa diupdate"));

        if (cutiCreateRequestDto.getTanggalSelesai().isBefore(cutiCreateRequestDto.getTanggalMulai())) {
            throw new CutiAlreadyExitsException("Tanggal selesai harus setelah tanggal mulai");
        }

        validateCutiUpdate(lastCuti, cutiCreateRequestDto);

        lastCuti.setTanggalMulai(cutiCreateRequestDto.getTanggalMulai());
        lastCuti.setTanggalSelesai(cutiCreateRequestDto.getTanggalSelesai());
        lastCuti.setAlasan(cutiCreateRequestDto.getAlasan());

        Cuti updated = cutiRepository.save(lastCuti);
        return ModelMapperUtil.convert(updated, CutiResponseDto.class);
    }

    public void deleteCutiTerakhir(Long pegawaiId) throws Exception {
        Cuti lastCuti = cutiRepository.findTopByPegawaiIdOrderByTanggalMulaiDesc(pegawaiId)
                .orElseThrow(() -> new CutiAlreadyExitsException("Tidak ada cuti yang bisa dihapus"));

        cutiRepository.delete(lastCuti);
    }

    private int getTotalHariCutiTahunIni(Long pegawaiId, int tahunMulai,int tahunSelesai) {
        List<Cuti> cutiList = cutiRepository.findByPegawaiIdAndYear(pegawaiId, tahunMulai,tahunSelesai);
        return cutiList.stream().mapToInt(Cuti::getDurasiHari).sum();
    }

    private void validateCuti(Long pegawaiId, CutiCreateRequestDto cutiCreateRequestDto) throws Exception {
        int totalHariCutiTahunIni = getTotalHariCutiTahunIni(pegawaiId, cutiCreateRequestDto.getTanggalMulai().getYear(),cutiCreateRequestDto.getTanggalSelesai().getYear());
        int durasiCutiBaru = cutiCreateRequestDto.getDurasiHari();

        if (totalHariCutiTahunIni + durasiCutiBaru > 12) {
            throw new CutiAlreadyExitsException("Maksimal cuti dalam setahun adalah 12 hari. Sisa jatah cuti: " + (12 - totalHariCutiTahunIni) + " hari");
        }

        if(cutiCreateRequestDto.getTanggalMulai().getMonthValue() == cutiCreateRequestDto.getTanggalSelesai().getMonthValue()){
            if(cutiCreateRequestDto.getDurasiHari() > 1 ){
                throw new CutiAlreadyExitsException("Hanya diperbolehkan cuti 1 hari per bulan");
            }
            boolean sudahCutiBulanIni = cutiRepository.existsByPegawaiIdAndMonthYear(
                    pegawaiId,
                    cutiCreateRequestDto.getTanggalMulai().getMonthValue(),
                    cutiCreateRequestDto.getTanggalMulai().getYear()
            );

            if (sudahCutiBulanIni) {
                throw new CutiAlreadyExitsException("Sudah ada cuti di bulan ini");
            }
        }

        if(cutiCreateRequestDto.getTanggalMulai().getMonthValue() != cutiCreateRequestDto.getTanggalSelesai().getMonthValue()){
            if(cutiCreateRequestDto.getDurasiHari() > 2 ){
                throw new CutiAlreadyExitsException("Hanya diperbolehkan cuti 1 hari per bulan");
            }
        }

        boolean sudahCutiBulanIni = cutiRepository.existsByPegawaiIdAndMonthYear2(
                pegawaiId,
                cutiCreateRequestDto.getTanggalMulai().getMonthValue(),
                cutiCreateRequestDto.getTanggalMulai().getYear()
        );

        if (sudahCutiBulanIni) {
            throw new CutiAlreadyExitsException("Sudah ada cuti di bulan ini");
        }
    }

    private void validateCutiUpdate(Cuti existingCuti, CutiCreateRequestDto dto) throws Exception {
        Long pegawaiId = dto.getPegawaiId();
        int tahunMulai = dto.getTanggalMulai().getYear();
        int tahunSelesai = dto.getTanggalSelesai().getYear();

        List<Cuti> cutiList = cutiRepository.findByPegawaiIdAndYear(pegawaiId, tahunMulai, tahunSelesai);
        int totalHariCuti = cutiList.stream()
                .filter(c -> !c.getId().equals(existingCuti.getId()))
                .mapToInt(Cuti::getDurasiHari)
                .sum();

        int durasiBaru = dto.getDurasiHari();

        if (totalHariCuti + durasiBaru > 12) {
            throw new CutiAlreadyExitsException("Maksimal cuti dalam setahun adalah 12 hari. Sisa jatah cuti: " + (12 - totalHariCuti));
        }

        int bulanMulai = dto.getTanggalMulai().getMonthValue();
        int bulanSelesai = dto.getTanggalSelesai().getMonthValue();

        if (bulanMulai == bulanSelesai) {
            if (durasiBaru > 1) {
                throw new CutiAlreadyExitsException("Hanya diperbolehkan cuti 1 hari per bulan");
            }

            boolean sudahCuti = cutiRepository.existsByPegawaiIdAndMonthYearExcludeId(pegawaiId, bulanMulai, tahunMulai, existingCuti.getId());
            if (sudahCuti) {
                throw new CutiAlreadyExitsException("Sudah ada cuti di bulan ini");
            }
        } else {
            if (durasiBaru > 2) {
                throw new CutiAlreadyExitsException("Hanya diperbolehkan cuti 1 hari per bulan (maks. 2 hari jika lintas bulan)");
            }

            boolean sudahCutiMulai = cutiRepository.existsByPegawaiIdAndMonthYearExcludeId(pegawaiId, bulanMulai, tahunMulai, existingCuti.getId());
            boolean sudahCutiSelesai = cutiRepository.existsByPegawaiIdAndMonthYearExcludeId(pegawaiId, bulanSelesai, tahunSelesai, existingCuti.getId());

            if (sudahCutiMulai || sudahCutiSelesai) {
                throw new CutiAlreadyExitsException("Sudah ada cuti di salah satu bulan yang diajukan");
            }
        }
    }

    public ResponseDataList<Cuti> search(CutiSearchRequestDto cutiSearchRequestDto) {
        Pageable pageable = PageRequest.of(
                cutiSearchRequestDto.getPage(),
                cutiSearchRequestDto.getLimit(),
                Sort.by(
                        Sort.Direction.fromString(cutiSearchRequestDto.getSortDirection()),
                        cutiSearchRequestDto.getSortBy()
                )
        );

        Page<Cuti> cutis = cutiRepository.findAll(SearchingUtil.build(cutiSearchRequestDto), pageable);

        return new ResponseDataList<>(
                MetadataMapperUtil.buildMetadata(cutis),
                cutis.getContent()
        );
    }

    public CutiResponseDto get(Long id) {
        Cuti cuti = cutiRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cuti with id " + id + " not found"));

        return ModelMapperUtil.convert(cuti,CutiResponseDto.class);
    }
}
