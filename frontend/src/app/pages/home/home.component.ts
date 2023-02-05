import { Component, OnInit } from '@angular/core';
import { ImageService } from 'src/app/services/image.service';
import { Image } from 'src/app/model/image';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ImageDialogComponent } from 'src/app/dialog/image-dialog/image-dialog.component';
import { SearchService } from 'src/app/services/search.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  images: Image[] = [];

  constructor(
    private imageService: ImageService,
    private imageDialog: MatDialog,
    private searchService: SearchService
  ) {}

  ngOnInit(): void {
    this.getUploadedImages("");
    this.searchService.getSearch().subscribe(keyword => {
      this.getUploadedImages(keyword);
    });
  }

  getUploadedImages(keyword: string): void {
    this.imageService.getImages(keyword).subscribe(response => {
      this.images = response;
    });
  }

  openImageDialog(image: Image): void {

    const dialog = this.imageDialog.open(ImageDialogComponent, {
      data: {
        id: image.id,
        name: image.name,
        size: image.size,
        url: image.url,
        contentType: image.contentType
      }
    });

    dialog.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }

}
