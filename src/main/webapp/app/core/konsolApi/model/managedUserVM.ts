/**
 * Konsol Core API
 * Konsol Core API documentation
 *
 * The version of the OpenAPI document: 0.0.1
 * Contact: adel50ali50@gmail.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

export interface ManagedUserVM {
  id?: string;
  login: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  imageUrl?: string;
  activated?: boolean;
  langKey?: string;
  createdBy?: string;
  createdDate?: string;
  lastModifiedBy?: string;
  lastModifiedDate?: string;
  authorities?: Set<string>;
  password?: string;
}
