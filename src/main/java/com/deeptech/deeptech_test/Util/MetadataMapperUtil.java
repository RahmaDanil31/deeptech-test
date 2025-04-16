package com.deeptech.deeptech_test.Util;

import com.deeptech.deeptech_test.BaseAsset.Metadata;
import org.springframework.data.domain.Page;

public class MetadataMapperUtil {

    public static Metadata buildMetadata(Page<?> object) {
        Metadata metadata = new Metadata();
        metadata.setPage(object.getNumber());
        metadata.setPerPage(object.getSize());
        metadata.setPageCount((int) Math.ceil((double) object.getTotalElements() / object.getSize()));
        metadata.setTotalCount(object.getTotalElements());
        return metadata;
    }
}
