import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Image } from 'src/app/model/image';

@Component({
  selector: 'app-image-dialog',
  templateUrl: './image-dialog.component.html',
  styleUrls: ['./image-dialog.component.scss']
})
export class ImageDialogComponent implements OnInit {

  image: Image;

  constructor(@Inject(MAT_DIALOG_DATA) public data: Image) { 
    this.image = data;
  }

  ngOnInit(): void {
  }

}
