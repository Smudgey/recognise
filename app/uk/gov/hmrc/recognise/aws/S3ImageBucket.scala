/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.recognise.aws

import scala.util.Try

trait AwsS3 {

  import com.amazonaws.regions.{Region, Regions}
  import com.amazonaws.services.s3.{AmazonS3, AmazonS3Client}
  import play.api.Logger

  val defaultRegion: String

  def s3Client(region: String = defaultRegion): AmazonS3 = {
    val s3 = new AmazonS3Client(Aws.credentials)
    s3.setRegion(Region.getRegion(Regions.fromName(region)))
    s3
  }

  def createBucket(name: String): Try[String] = {
    Try {
      val s3 = s3Client()
      if (!s3.doesBucketExist(name)) {
        Logger.debug(s"Creating bucket with $name")
        s3.createBucket(name)
      }
      val location = s3.getBucketLocation(name)
      Logger.debug(s"Bucket created at location : $location")
      location
    }
  }
}

trait S3ImageBucket extends AwsS3 {

  import com.amazonaws.services.rekognition.model.{Image, S3Object}

  def getImage(imageName: String, bucketName: String) = {
    createBucket(bucketName)
    new Image()
      .withS3Object(new S3Object()
        .withBucket(bucketName)
        .withName(imageName))
  }
}

object S3ImageBucket extends S3ImageBucket {
  override val defaultRegion: String = Aws.defaultRegion
}
