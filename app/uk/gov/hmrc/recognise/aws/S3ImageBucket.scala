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

trait S3ImageBucket {

  import com.amazonaws.services.rekognition.model.{Image, S3Object}

  val bucketName: String

  def getImage(imageName: String) = {
    new Image()
      .withS3Object(new S3Object()
        .withBucket(bucketName)
        .withName(imageName))
  }
}

object S3ImageBucket extends S3ImageBucket {
  override val bucketName: String = ???
}
