import { Injectable } from '@angular/core';
import { FileTransfer, FileUploadOptions, FileTransferObject, FileUploadResult } from '@ionic-native/file-transfer';
import { Config } from '../../app/config';


@Injectable()
export class FileUploadService {

    public attachUploadUrl: string = "/api/upload/uploadFiles";
  
    public fileTransfer: FileTransferObject;
    constructor(
        public transfer: FileTransfer,
        public config:Config
    ) {
      this.fileTransfer = this.transfer.create();
      this.attachUploadUrl = this.config.API + this.attachUploadUrl;
    }


    uploadFile(filePath): Promise<FileUploadResult>{
        let $this = this;
        return new Promise(function(resolve, reject) {
          let options: FileUploadOptions = {
            fileKey: 'file',
            fileName: 'jpg',//文件后缀
            mimeType: "image/jpeg",//文件格式，默认为image/jpeg
            params: {}, //参数
            headers: {}
         }
         let fileTypeId = '2';//公司平台附件上传默认分类
         options.params = {fileTypeId:fileTypeId};
      
         $this.fileTransfer.upload(filePath, $this.attachUploadUrl, options)
          .then((data) => {
            // success
              resolve(data);
           }, (err) => {
             // error
              reject(err);
              // alert("上传失败");
          });
      
          // 进度
         $this.fileTransfer.onProgress(progressEvent => {
             console.log(progressEvent)
             if (progressEvent.lengthComputable) {
               // 下载过程会一直打印，完成的时候会显示 1
               console.log(progressEvent.loaded / progressEvent.total);
             } else {
               console.log("3333333333")
             }
           });
        });
    }




    getFileType(fileName: string): string {
      return fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length).toLowerCase();
    }
  
    getFileMimeType(fileType: string): string {
      let mimeType: string = '';
      switch (fileType) {
        case 'txt':
          mimeType = 'text/plain';
          break;
        case 'docx':
          mimeType = 'application/vnd.openxmlformats-officedocument.wordprocessingml.document';
          break;
        case 'doc':
          mimeType = 'application/msword';
          break;
        case 'pptx':
          mimeType = 'application/vnd.openxmlformats-officedocument.presentationml.presentation';
          break;
        case 'ppt':
          mimeType = 'application/vnd.ms-powerpoint';
          break;
        case 'xlsx':
          mimeType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
          break;
        case 'xls':
          mimeType = 'application/vnd.ms-excel';
          break;
        case 'zip':
          mimeType = 'application/x-zip-compressed';
          break;
        case 'rar':
          mimeType = 'application/octet-stream';
          break;
        case 'pdf':
          mimeType = 'application/pdf';
          break;
        case 'jpg':
          mimeType = 'image/jpeg';
          break;
        case 'png':
          mimeType = 'image/png';
          break;
        default:
          mimeType = 'application/' + fileType;
          break;
      }
      return mimeType;
    }
}


