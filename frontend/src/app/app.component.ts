import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { UploadDialogComponent } from './dialog/upload-dialog/upload-dialog.component';
import { SearchService } from './services/search.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'frontend';

  searchKeyword: string = "";

  constructor(
    private uploadDialog: MatDialog,
    private searchService: SearchService
  ) {}

  openUploadDialog(): void {
    const dialog = this.uploadDialog.open(UploadDialogComponent);

    dialog.afterClosed().subscribe(result => {
      if (result) {
        setTimeout(() => {
          this.searchService.updateSearch(this.searchKeyword);
        }, 200);
      }
    });
    
  }

  searchImages(event: any): void {
    this.searchKeyword = event.target.value;
    this.searchService.updateSearch(this.searchKeyword);
  }

}
