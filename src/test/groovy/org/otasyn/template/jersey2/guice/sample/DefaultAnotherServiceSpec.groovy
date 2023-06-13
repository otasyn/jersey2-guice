package org.otasyn.template.jersey2.guice.sample

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

import jakarta.inject.Provider
import jakarta.inject.Inject

import spock.guice.UseModules
import spock.lang.Specification
import spock.lang.Unroll

import org.otasyn.template.jersey2.guice.MockGuiceModule

@Unroll
@UseModules(MockGuiceModule)
class DefaultAnotherServiceSpec extends Specification {

  @Inject Provider<DefaultAnotherService> toTestProvider
  def toTest

  def setup() {

    toTest = toTestProvider.get()

  }

  def 'provides a name' () {
    when:
      def name = toTest.provideName()

    then:
      name == 'from another service'
  }

}
