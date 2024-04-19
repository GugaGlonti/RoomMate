package de.hhu.propra.roommate.architecture;

import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import de.hhu.propra.roommate.RoomMateApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;


@AnalyzeClasses(packagesOf = RoomMateApplication.class)
public class ArcTests {

  @ArchTest
  ArchRule noCircularDependencies = slices()
      .matching("..(application).(*)..")
      .should()
      .beFreeOfCycles();

  @ArchTest
  ArchRule servicesAnnotations = classes()
      .that()
      .areAnnotatedWith(Service.class)
      .should()
      .resideInAPackage("..application.service..");

  @ArchTest
  ArchRule repositoryAnnotations = classes()
      .that()
      .areAnnotatedWith(Repository.class)
      .should()
      .resideInAPackage("..adapter.db..");

  @ArchTest
  ArchRule privateControllerFields = fields()
      .that()
      .areDeclaredInClassesThat()
      .areAnnotatedWith(Controller.class)
      .should()
      .bePrivate();

  @ArchTest
  ArchRule servicesDontDependOnRepos = noClasses()
      .that()
      .resideInAPackage("..application.service..")
      .should()
      .dependOnClassesThat()
      .resideInAPackage("..adapter.db..");

  @ArchTest
  ArchRule controllerNamingConvention = classes()
      .that()
      .resideInAPackage("..web..")
      .and()
      .areNotAnnotatedWith(ControllerAdvice.class)
      .should()
      .haveSimpleNameEndingWith("Controller");

  @ArchTest
  ArchRule serviceNamingConvention = classes()
      .that()
      .resideInAPackage("..application.service..")
      .and()
      .areAnnotatedWith(Service.class)
      .should()
      .haveSimpleNameEndingWith("Service");

  @ArchTest
  ArchRule allControllerClassesUseConstructorInjection = fields()
      .that()
      .areDeclaredInClassesThat()
      .resideInAPackage("..adapter.web..")
      .should()
      .beAnnotatedWith(Autowired.class)
      .orShould()
      .haveModifier(JavaModifier.PRIVATE)
      .as("Only use constructor injection or private fields.");

  @ArchTest
  ArchRule noDeprecatedAnnotations = noClasses()
      .should()
      .beAnnotatedWith(Deprecated.class);

}
