import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Image } from '../model/image';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private http:HttpClient) { }

  upload(file: File): Observable<any> {

    const formData = new FormData();

    formData.append("file", file, file.name);

    return this.http.post("http://localhost:8080/files", formData);
  }

  getImages(keyword: string): Observable<Image[]> {
    return this.http.get<Image[]>("http://localhost:8080/files/search?keyword="+keyword);
  }
}