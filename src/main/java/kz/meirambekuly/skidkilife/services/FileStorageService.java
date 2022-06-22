package kz.meirambekuly.skidkilife.services;

import kz.meirambekuly.skidkilife.web.dto.ResultDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    ResultDto<?> saveEstablishmentImage (Long establishmentId, MultipartFile multipartFile);

    void deleteEstablishmentImage (Long establishmentId);

    ResultDto<?> saveSaleImage (Long saleId, MultipartFile multipartFile);

    void deleteSaleImage (Long saleId);

}
