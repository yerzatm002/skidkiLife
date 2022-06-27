package kz.meirambekuly.skidkilife.services.impl;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import kz.meirambekuly.skidkilife.constants.Errors;
import kz.meirambekuly.skidkilife.entity.Establishment;
import kz.meirambekuly.skidkilife.entity.Sale;
import kz.meirambekuly.skidkilife.repositories.EstablishmentRepository;
import kz.meirambekuly.skidkilife.repositories.SaleRepository;
import kz.meirambekuly.skidkilife.services.FileStorageService;
import kz.meirambekuly.skidkilife.web.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IFileStorageService implements FileStorageService {

    @Value("${application.bucket-name}")
    private String BUCKET_NAME;

    @Value("${application.download-url}")
    private String DOWNLOAD_URL;

    private final EstablishmentRepository establishmentRepository;
    private final SaleRepository saleRepository;

    @Override
    public ResultDto<?> saveEstablishmentImage(Long establishmentId, MultipartFile multipartFile) {
        Optional<Establishment> establishment = establishmentRepository.findById(establishmentId);
        if (establishment.isPresent()) {
            if (!multipartFile.isEmpty()) {
                UUID uuid;
                try {
                    uuid = UUID.nameUUIDFromBytes(multipartFile.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String fileName = uuid.toString();
                File file = this.convertToFile(multipartFile, fileName);
                String TEMP_URL = this.saveFile(file, fileName);
                boolean isDeleted = file.delete();
                if (isDeleted) {
                    establishment.get().setImage(TEMP_URL);
                    establishmentRepository.save(establishment.get());
                    return ResultDto.builder()
                            .isSuccess(true)
                            .HttpStatus(HttpStatus.OK.value())
                            .data(TEMP_URL)
                            .build();
                }
                return ResultDto.builder()
                        .isSuccess(false)
                        .HttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errorMessage(Errors.ERR_INTERNAL_SERVER_ERROR)
                        .build();
            }
            return ResultDto.builder()
                    .isSuccess(false)
                    .HttpStatus(HttpStatus.NO_CONTENT.value())
                    .errorMessage(Errors.ERR_VALIDATION)
                    .build();
        }
            return ResultDto.builder()
                    .isSuccess(false)
                    .HttpStatus(HttpStatus.NOT_FOUND.value())
                    .errorMessage(Errors.NO_USER)
                    .build();
    }

    @Override
    public void deleteEstablishmentImage(Long establishmentId) {
        try {
            Optional<Establishment> establishment = establishmentRepository.findById(establishmentId);
            establishment.ifPresent(val -> {
                String establishmentImage = val.getImage();
                if (establishmentImage.isEmpty()) {
                    throw new RuntimeException(Errors.FORBIDDEN_ACTION);
                }
                FileInputStream serviceAccount;
                try {
                    serviceAccount = new FileInputStream("./src/main/resources/firebase.json");
                    FirebaseOptions options = new FirebaseOptions.Builder()
                            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                            .build();
                    FirebaseApp.initializeApp(options);
                    Bucket bucket = StorageClient.getInstance().bucket(BUCKET_NAME);
                    Blob blob = bucket.get(establishmentImage.substring(72, establishmentImage.length() - 10));
                    blob.delete();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                val.setImage(null);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultDto<?> saveSaleImage(Long saleId, MultipartFile multipartFile) {
        Optional<Sale> sale = saleRepository.findById(saleId);
        if (sale.isPresent()) {
            if (!multipartFile.isEmpty()) {
                UUID uuid;
                try {
                    uuid = UUID.nameUUIDFromBytes(multipartFile.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String fileName = uuid.toString();
                File file = this.convertToFile(multipartFile, fileName);
                String TEMP_URL = this.saveFile(file, fileName);
                boolean isDeleted = file.delete();
                if (isDeleted) {
                    sale.get().setImage(TEMP_URL);
                    saleRepository.save(sale.get());
                    return ResultDto.builder()
                            .isSuccess(true)
                            .HttpStatus(HttpStatus.OK.value())
                            .data(TEMP_URL)
                            .build();
                }
                return ResultDto.builder()
                        .isSuccess(false)
                        .HttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errorMessage(Errors.ERR_INTERNAL_SERVER_ERROR)
                        .build();
            }
            return ResultDto.builder()
                    .isSuccess(false)
                    .HttpStatus(HttpStatus.NO_CONTENT.value())
                    .errorMessage(Errors.ERR_VALIDATION)
                    .build();
        }
        return ResultDto.builder()
                .isSuccess(false)
                .HttpStatus(HttpStatus.NOT_FOUND.value())
                .errorMessage(Errors.NOT_VALID_FIELDS)
                .build();
    }

    @Override
    public void deleteSaleImage(Long saleId) {
        try {
            Optional<Sale> sale = saleRepository.findById(saleId);
            sale.ifPresent(val -> {
                String saleImage = val.getImage();
                if (saleImage.isEmpty()) {
                    throw new RuntimeException(Errors.FORBIDDEN_ACTION);
                }
                FileInputStream serviceAccount;
                try {
                    serviceAccount = new FileInputStream("./src/main/resources/firebase.json");
                    FirebaseOptions options = new FirebaseOptions.Builder()
                            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                            .build();
                    FirebaseApp.initializeApp(options);
                    Bucket bucket = StorageClient.getInstance().bucket(BUCKET_NAME);
                    Blob blob = bucket.get(saleImage.substring(72, saleImage.length() - 10));
                    blob.delete();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                val.setImage(null);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to check type of file
     *
     * @param file File
     * @return boolean flag
     */
//    private boolean flagImage(MultipartFile file) {
//        if (Objects.nonNull(file.getContentType())) {
//            String type = file.getContentType().split("/")[0];
//            return type.equals(Fltps.TYPE_IMAGE);
//        }
//        return false;
//    }

    /**
     * Converting Multipartfile into the file
     *
     * @param multipartFile file that we wanted to convert
     * @param fileName      name of file
     * @return File
     */
    private File convertToFile(MultipartFile multipartFile, String fileName) {
        File file = new File(fileName);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * Save file into the firebase storage
     *
     * @param file     File
     * @param fileName name of file
     * @return url of uploaded file
     */
    private String saveFile(File file, String fileName) {
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials;
        try {
            credentials = GoogleCredentials
                    .fromStream(new FileInputStream("./src/main/resources/firebase.json"));
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

}
