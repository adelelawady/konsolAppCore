/**
 * Konsol Core API
 * Konsol Core API documentation
 *
 * OpenAPI spec version: 0.0.1
 * Contact: adel50ali50@gmail.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */ /* tslint:disable:no-unused-variable member-ordering */

import { Inject, Injectable, Optional } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse, HttpEvent } from '@angular/common/http';
import { CustomHttpUrlEncodingCodec } from '../encoder';

import { Observable } from 'rxjs';

import { AdminUserDTO } from '../model/adminUserDTO';
import { KeyAndPasswordVM } from '../model/keyAndPasswordVM';
import { ManagedUserVM } from '../model/managedUserVM';
import { PasswordChangeDTO } from '../model/passwordChangeDTO';

import { BASE_PATH, COLLECTION_FORMATS } from '../variables';
import { Configuration } from '../configuration';

@Injectable()
export class AccountResourceService {
  protected basePath = 'http://localhost:8080/api';
  public defaultHeaders = new HttpHeaders();
  public configuration = new Configuration();

  constructor(protected httpClient: HttpClient, @Optional() @Inject(BASE_PATH) basePath: string, @Optional() configuration: Configuration) {
    if (basePath) {
      this.basePath = basePath;
    }
    if (configuration) {
      this.configuration = configuration;
      this.basePath = basePath || configuration.basePath || this.basePath;
    }
  }

  /**
   * @param consumes string[] mime-types
   * @return true: consumes contains 'multipart/form-data', false: otherwise
   */
  private canConsumeForm(consumes: string[]): boolean {
    const form = 'multipart/form-data';
    for (const consume of consumes) {
      if (form === consume) {
        return true;
      }
    }
    return false;
  }

  /**
   *
   *
   * @param key
   * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
   * @param reportProgress flag to report request and response progress.
   */
  public activateAccount(key: string, observe?: 'body', reportProgress?: boolean): Observable<any>;
  public activateAccount(key: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
  public activateAccount(key: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;
  public activateAccount(key: string, observe: any = 'body', reportProgress: boolean = false): Observable<any> {
    if (key === null || key === undefined) {
      throw new Error('Required parameter key was null or undefined when calling activateAccount.');
    }

    let queryParameters = new HttpParams({ encoder: new CustomHttpUrlEncodingCodec() });
    if (key !== undefined && key !== null) {
      queryParameters = queryParameters.set('key', <any>key);
    }

    let headers = this.defaultHeaders;

    // to determine the Accept header
    let httpHeaderAccepts: string[] = [];
    const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
    if (httpHeaderAcceptSelected != undefined) {
      headers = headers.set('Accept', httpHeaderAcceptSelected);
    }

    // to determine the Content-Type header
    const consumes: string[] = [];

    return this.httpClient.request<any>('get', `${this.basePath}/activate`, {
      params: queryParameters,
      withCredentials: this.configuration.withCredentials,
      headers: headers,
      observe: observe,
      reportProgress: reportProgress,
    });
  }

  /**
   *
   *
   * @param body
   * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
   * @param reportProgress flag to report request and response progress.
   */
  public changePassword(body: PasswordChangeDTO, observe?: 'body', reportProgress?: boolean): Observable<any>;
  public changePassword(body: PasswordChangeDTO, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
  public changePassword(body: PasswordChangeDTO, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;
  public changePassword(body: PasswordChangeDTO, observe: any = 'body', reportProgress: boolean = false): Observable<any> {
    if (body === null || body === undefined) {
      throw new Error('Required parameter body was null or undefined when calling changePassword.');
    }

    let headers = this.defaultHeaders;

    // to determine the Accept header
    let httpHeaderAccepts: string[] = [];
    const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
    if (httpHeaderAcceptSelected != undefined) {
      headers = headers.set('Accept', httpHeaderAcceptSelected);
    }

    // to determine the Content-Type header
    const consumes: string[] = ['application/json'];
    const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
    if (httpContentTypeSelected != undefined) {
      headers = headers.set('Content-Type', httpContentTypeSelected);
    }

    return this.httpClient.request<any>('post', `${this.basePath}/account/change-password`, {
      body: body,
      withCredentials: this.configuration.withCredentials,
      headers: headers,
      observe: observe,
      reportProgress: reportProgress,
    });
  }

  /**
   *
   *
   * @param body
   * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
   * @param reportProgress flag to report request and response progress.
   */
  public finishPasswordReset(body: KeyAndPasswordVM, observe?: 'body', reportProgress?: boolean): Observable<any>;
  public finishPasswordReset(body: KeyAndPasswordVM, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
  public finishPasswordReset(body: KeyAndPasswordVM, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;
  public finishPasswordReset(body: KeyAndPasswordVM, observe: any = 'body', reportProgress: boolean = false): Observable<any> {
    if (body === null || body === undefined) {
      throw new Error('Required parameter body was null or undefined when calling finishPasswordReset.');
    }

    let headers = this.defaultHeaders;

    // to determine the Accept header
    let httpHeaderAccepts: string[] = [];
    const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
    if (httpHeaderAcceptSelected != undefined) {
      headers = headers.set('Accept', httpHeaderAcceptSelected);
    }

    // to determine the Content-Type header
    const consumes: string[] = ['application/json'];
    const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
    if (httpContentTypeSelected != undefined) {
      headers = headers.set('Content-Type', httpContentTypeSelected);
    }

    return this.httpClient.request<any>('post', `${this.basePath}/account/reset-password/finish`, {
      body: body,
      withCredentials: this.configuration.withCredentials,
      headers: headers,
      observe: observe,
      reportProgress: reportProgress,
    });
  }

  /**
   *
   *
   * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
   * @param reportProgress flag to report request and response progress.
   */
  public getAccount(observe?: 'body', reportProgress?: boolean): Observable<AdminUserDTO>;
  public getAccount(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<AdminUserDTO>>;
  public getAccount(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<AdminUserDTO>>;
  public getAccount(observe: any = 'body', reportProgress: boolean = false): Observable<any> {
    let headers = this.defaultHeaders;

    // to determine the Accept header
    let httpHeaderAccepts: string[] = ['application/json'];
    const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
    if (httpHeaderAcceptSelected != undefined) {
      headers = headers.set('Accept', httpHeaderAcceptSelected);
    }

    // to determine the Content-Type header
    const consumes: string[] = [];

    return this.httpClient.request<AdminUserDTO>('get', `${this.basePath}/account`, {
      withCredentials: this.configuration.withCredentials,
      headers: headers,
      observe: observe,
      reportProgress: reportProgress,
    });
  }

  /**
   *
   *
   * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
   * @param reportProgress flag to report request and response progress.
   */
  public isAuthenticated(observe?: 'body', reportProgress?: boolean): Observable<string>;
  public isAuthenticated(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<string>>;
  public isAuthenticated(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<string>>;
  public isAuthenticated(observe: any = 'body', reportProgress: boolean = false): Observable<any> {
    let headers = this.defaultHeaders;

    // to determine the Accept header
    let httpHeaderAccepts: string[] = ['application/json'];
    const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
    if (httpHeaderAcceptSelected != undefined) {
      headers = headers.set('Accept', httpHeaderAcceptSelected);
    }

    // to determine the Content-Type header
    const consumes: string[] = [];

    return this.httpClient.request<string>('get', `${this.basePath}/authenticate`, {
      withCredentials: this.configuration.withCredentials,
      headers: headers,
      observe: observe,
      reportProgress: reportProgress,
    });
  }

  /**
   *
   *
   * @param body
   * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
   * @param reportProgress flag to report request and response progress.
   */
  public registerAccount(body: ManagedUserVM, observe?: 'body', reportProgress?: boolean): Observable<any>;
  public registerAccount(body: ManagedUserVM, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
  public registerAccount(body: ManagedUserVM, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;
  public registerAccount(body: ManagedUserVM, observe: any = 'body', reportProgress: boolean = false): Observable<any> {
    if (body === null || body === undefined) {
      throw new Error('Required parameter body was null or undefined when calling registerAccount.');
    }

    let headers = this.defaultHeaders;

    // to determine the Accept header
    let httpHeaderAccepts: string[] = [];
    const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
    if (httpHeaderAcceptSelected != undefined) {
      headers = headers.set('Accept', httpHeaderAcceptSelected);
    }

    // to determine the Content-Type header
    const consumes: string[] = ['application/json'];
    const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
    if (httpContentTypeSelected != undefined) {
      headers = headers.set('Content-Type', httpContentTypeSelected);
    }

    return this.httpClient.request<any>('post', `${this.basePath}/register`, {
      body: body,
      withCredentials: this.configuration.withCredentials,
      headers: headers,
      observe: observe,
      reportProgress: reportProgress,
    });
  }

  /**
   *
   *
   * @param body
   * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
   * @param reportProgress flag to report request and response progress.
   */
  public requestPasswordReset(body: string, observe?: 'body', reportProgress?: boolean): Observable<any>;
  public requestPasswordReset(body: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
  public requestPasswordReset(body: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;
  public requestPasswordReset(body: string, observe: any = 'body', reportProgress: boolean = false): Observable<any> {
    if (body === null || body === undefined) {
      throw new Error('Required parameter body was null or undefined when calling requestPasswordReset.');
    }

    let headers = this.defaultHeaders;

    // to determine the Accept header
    let httpHeaderAccepts: string[] = [];
    const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
    if (httpHeaderAcceptSelected != undefined) {
      headers = headers.set('Accept', httpHeaderAcceptSelected);
    }

    // to determine the Content-Type header
    const consumes: string[] = ['application/json'];
    const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
    if (httpContentTypeSelected != undefined) {
      headers = headers.set('Content-Type', httpContentTypeSelected);
    }

    return this.httpClient.request<any>('post', `${this.basePath}/account/reset-password/init`, {
      body: body,
      withCredentials: this.configuration.withCredentials,
      headers: headers,
      observe: observe,
      reportProgress: reportProgress,
    });
  }

  /**
   *
   *
   * @param body
   * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
   * @param reportProgress flag to report request and response progress.
   */
  public saveAccount(body: AdminUserDTO, observe?: 'body', reportProgress?: boolean): Observable<any>;
  public saveAccount(body: AdminUserDTO, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
  public saveAccount(body: AdminUserDTO, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;
  public saveAccount(body: AdminUserDTO, observe: any = 'body', reportProgress: boolean = false): Observable<any> {
    if (body === null || body === undefined) {
      throw new Error('Required parameter body was null or undefined when calling saveAccount.');
    }

    let headers = this.defaultHeaders;

    // to determine the Accept header
    let httpHeaderAccepts: string[] = [];
    const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
    if (httpHeaderAcceptSelected != undefined) {
      headers = headers.set('Accept', httpHeaderAcceptSelected);
    }

    // to determine the Content-Type header
    const consumes: string[] = ['application/json'];
    const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
    if (httpContentTypeSelected != undefined) {
      headers = headers.set('Content-Type', httpContentTypeSelected);
    }

    return this.httpClient.request<any>('post', `${this.basePath}/account`, {
      body: body,
      withCredentials: this.configuration.withCredentials,
      headers: headers,
      observe: observe,
      reportProgress: reportProgress,
    });
  }
}
