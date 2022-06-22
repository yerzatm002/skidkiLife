package kz.meirambekuly.skidkilife.services.impl;

import kz.meirambekuly.skidkilife.services.FileStorageService;
import kz.meirambekuly.skidkilife.web.dto.ResultDto;
import org.springframework.web.multipart.MultipartFile;

public class IFileStorageService implements FileStorageService {
    @Override
    public ResultDto<?> saveEstablishmentImage(Long establishmentId, MultipartFile multipartFile) {
        return null;
    }

    @Override
    public void deleteEstablishmentImage(Long establishmentId) {

    }

    @Override
    public ResultDto<?> saveSaleImage(Long saleId, MultipartFile multipartFile) {
        return null;
    }

    @Override
    public void deleteSaleImage(Long saleId) {

    }
}
