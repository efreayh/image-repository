import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private subject = new Subject<string>();

  constructor() { }

  updateSearch(keyword: string) {
    this.subject.next(keyword);
  }

  getSearch(): Observable<string> {
    return this.subject.asObservable();
  }

}
