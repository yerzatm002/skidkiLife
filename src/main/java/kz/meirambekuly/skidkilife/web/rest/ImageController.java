package kz.meirambekuly.skidkilife.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kz.meirambekuly.skidkilife.services.FileStorageService;
import kz.meirambekuly.skidkilife.utilities.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Constants.PRIVATE_ENDPOINT + "/image")
@RequiredArgsConstructor
public class ImageController {

    private final FileStorageService fileStorageService;

    @PostMapping("/uploadImagesForEstablishment")
    @ApiOperation(value = "upload images for establishment")
    public ResponseEntity<?> uploadImagesForEstablishment(@ApiParam(value = "image") @RequestParam("file") MultipartFile file,
                                                       @ApiParam(value = "establishment id")
                                                       @RequestParam("establishmentId") Long establishmentId) {
        return ResponseEntity.ok(fileStorageService.saveEstablishmentImage(establishmentId, file));
    }

    @PostMapping("/uploadImagesForSale")
    @ApiOperation(value = "upload images for sales")
    public ResponseEntity<?> uploadImagesForSale(@ApiParam(value = "image") @RequestParam("file") MultipartFile file,
                                                       @ApiParam(value = "sale id")
                                                       @RequestParam("saleId") Long saleId) {
        return ResponseEntity.ok(fileStorageService.saveSaleImage(saleId, file));
    }

    @DeleteMapping("/deleteEstablishmentImage")
    @ApiOperation(value = "Delete Establishment image")
    @Timed
    public ResponseEntity<?> removeEstablishmentImage(@ApiParam(value = "establishment id") @RequestParam("id") Long id){
        fileStorageService.deleteEstablishmentImage(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteSaleImage")
    @ApiOperation(value = "Delete Sale image")
    @Timed
    public ResponseEntity<?> removeSaleImage(@ApiParam(value = "sale id") @RequestParam("id") Long id){
        fileStorageService.deleteSaleImage(id);
        return ResponseEntity.ok().build();
    }

}
