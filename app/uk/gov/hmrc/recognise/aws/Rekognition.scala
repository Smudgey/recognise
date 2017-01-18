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








trait Rekognition {

  import com.amazonaws.services.rekognition.AmazonRekognitionClient
  import com.amazonaws.services.rekognition.model.{CompareFacesRequest, CompareFacesResult, Image}

  val serviceEndpoint: String

  val faceSimilarityThreshold: Int

  lazy val client: AmazonRekognitionClient = new AmazonRekognitionClient(Aws.credentials).withEndpoint(serviceEndpoint)

  //  def detectLabels

  //  def detectFaces

  def compareFaces(source: => Image, target: => Image): CompareFacesResult = {
    val compareFacesRequest = new CompareFacesRequest()
      .withSourceImage(source)
      .withTargetImage(target)
      .withSimilarityThreshold(faceSimilarityThreshold.toFloat)
    client.compareFaces(compareFacesRequest)
  }
}

object Rekognition extends Rekognition {

  override val serviceEndpoint: String = Aws.baseUrl("rekognition")

  override val faceSimilarityThreshold: Int = Aws.getConfInt("aws.rekognition.thresholds.faceComparison.confidence")
}
