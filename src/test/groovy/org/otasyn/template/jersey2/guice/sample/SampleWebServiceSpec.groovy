package org.otasyn.template.jersey2.guice.sample

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

import jakarta.inject.Inject
import jakarta.inject.Provider

import spock.guice.UseModules
import spock.lang.Specification
import spock.lang.Unroll

import org.otasyn.template.jersey2.guice.MockGuiceModule
import org.otasyn.template.jersey2.guice.sample.SampleService
import org.otasyn.template.jersey2.guice.sample.SampleWebService

@Unroll
@UseModules(MockGuiceModule)
class SampleWebServiceSpec extends Specification {

  @Inject SampleService sampleService

  @Inject Provider<SampleWebService> toTestProvider
  def toTest

  def setup() {
    toTest = toTestProvider.get()
  }

  def 'get a message' () {
    given:
      def sampleMessage = 'blah'

    when:
      def message = toTest.get()

    then:
      message == sampleMessage
      1 * sampleService.getMessage() >> sampleMessage
  }

  def 'get a message from the path' () {
    given:
      def pathParam = 'blah'

    when:
      def message = toTest.getWithMessage(pathParam)

    then:
      message == "Path message: ${pathParam}"
  }

  def 'get a message using typesafe config' () {
    given:
      def configMessage = 'blah'

    when:
      def message = toTest.getWithTypesafeConfigMessage()

    then:
      message == configMessage
      1 * sampleService.getTypesafeConfigMessage() >> configMessage
  }
}
