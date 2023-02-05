import { Component, OnInit } from '@angular/core';
import { ImageService } from 'src/app/services/image.service';

@Component({
  selector: 'app-upload-dialog',
  templateUrl: './upload-dialog.component.html',
  styleUrls: ['./upload-dialog.component.scss']
})
export class UploadDialogComponent implements OnInit {

  shortLink: string = "";
  loading: boolean = false;
  file: File = null!;

  constructor(private imageService: ImageService) { }

  ngOnInit(): void {
  }

  onChange(event: Event): void {
    this.file = (event.target as HTMLInputElement).files![0];
  }

  onUpload() {
    this.loading = !this.loading;
    console.log(this.file);
    this.imageService.upload(this.file).subscribe(
      (event: any) => {
        if (typeof (event) === 'object') {
          this.shortLink = event.link;
          this.loading = false;
        }
      }
    );
  }

}
