package de.hhu.propra.roommate.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import de.hhu.propra.roommate.RoomMateApplication;
import org.junit.jupiter.api.DisplayName;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@DisplayName("Architektur")
@AnalyzeClasses(packagesOf = RoomMateApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class OnionArchitectureTest {

  @ArchTest
  ArchRule onionArchitektur = onionArchitecture()
      .domainModels("..domain.model..")
      .domainServices("..domain.service..")
      .applicationServices("..application.service..")
      .adapter("web", "..adapter.web..")
      .adapter("db", "..adapter.db..")
      .adapter("config", "..adapter.config..");

}
