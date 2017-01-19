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

import java.io.File

import org.scalatest.Ignore
import play.api.Logger
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

@Ignore
class RekognitionUsageSpike extends UnitSpec with WithFakeApplication {

  import Rekognition._
  import scala.concurrent.ExecutionContext.Implicits.global

  val bucketName = "hmrc-rekognition-spike"

  def evidence = new File(getClass.getClassLoader.getResource("samples/passport.jpg").toURI)
  def profile1 = new File(getClass.getClassLoader.getResource("samples/profile-1.jpg").toURI)
  def profile2 = new File(getClass.getClassLoader.getResource("samples/profile-2.jpg").toURI)
  def profileInAGroup = new File(getClass.getClassLoader.getResource("samples/profile-in-group.jpg").toURI)

  "Comparing faces" should {

    "produce a match" in {

      val results = compareFaces(evidence, profile1)

      results.getFaceMatches.foreach(fm => Logger.info(s"Face match : ${fm.toString}"))
    }
  }


}
