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

import scala.util.{Failure, Success}


trait Aws {

  import com.amazonaws.auth.AWSCredentials
  import com.amazonaws.auth.profile.ProfileCredentialsProvider

  import scala.util.Try

  val profileName: String

  val defaultRegion : String

  lazy val credentials: AWSCredentials =
    Try(new ProfileCredentialsProvider(profileName).getCredentials) match {
      case Success(credential) => credential
      case Failure(t) => throw new RuntimeException(s"Failed to find the credentials for the profile : '$profileName'", t)
    }
}

object Aws extends Aws {

  import play.api.Play
  import play.api.Play.current

  override val profileName: String = getConfString("aws.credentials.profile", throw new RuntimeException(s"Could not find config 'aws.credentials.profile'"))

  override val defaultRegion : String = maybeDefaultRegion.getOrElse(throw new RuntimeException(s"Could not find config aws.defaultRegion"))

  private lazy val maybeDefaultRegion : Option[String] = Play.configuration.getString("aws.defaultRegion")

  def baseUrl(serviceName: String) : String = {
    val protocol = getConfString(s"aws.$serviceName.protocol", throw new RuntimeException(s"Could not find config aws.$serviceName.protocol"))
    val host = getConfString(s"aws.$serviceName.service", throw new RuntimeException(s"Could not find config aws.$serviceName.service"))
    val awsRegion = region(serviceName)
    s"$protocol://$host.$awsRegion.amazonaws.com"
  }

  private def region(serviceName: String) : String =
    Play.configuration.getString(s"aws.$serviceName.region")
        .getOrElse(maybeDefaultRegion
          .getOrElse(throw new RuntimeException(s"Could not find config aws.$serviceName.region")))

  def getConfString(confKey: String, defString: => String): String = Play.configuration.getString(confKey).getOrElse(defString)

  def getConfInt(confKey: String): Int = getConfInt(confKey, throw new RuntimeException(s"Could not find config aws.$confKey"))

  def getConfInt(confKey: String, defInt: => Int): Int = Play.configuration.getInt(confKey).getOrElse(defInt)
}

