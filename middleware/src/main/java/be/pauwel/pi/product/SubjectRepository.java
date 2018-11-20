package be.pauwel.pi.product;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "subject", path = "subject")
public interface SubjectRepository extends PagingAndSortingRepository<Subject, Long> {

    List<Subject> findByNaamNL(@Param("name") String name);
    List<Subject> findByNaamFR(@Param("name") String name);
    List<Subject> findByOmschrijvingNL(@Param("name") String name);
    List<Subject> findByOmschrijvingFR(@Param("name") String name);
    List<Subject> findByBrand(@Param("name") String name);
    List<Subject> findByProductnorm(@Param("name") String name);
    List<Subject> findByTypeKort(@Param("name") String name);
    List<Subject> findByTypeLangNL(@Param("name") String name);
    List<Subject> findByTypeLangFR(@Param("name") String name);
    List<Subject> findByReactieBijBrand(@Param("name") String name);
    List<Subject> findByReactieBijBrandNorm(@Param("name") String name);
//    List<Subject> findByDOPNummer(@Param("name") String name);
    List<Subject> findByKantvorm(@Param("name") String name);
    List<Subject> findByDikte(@Param("name") String name);
    List<Subject> findByBreedte(@Param("name") String name);
    List<Subject> findByLengte(@Param("name") String name);
    List<Subject> findByHoogte(@Param("name") String name);
    List<Subject> findByDikteIsolatie(@Param("name") String name);
    List<Subject> findByTotaleDikte(@Param("name") String name);
    List<Subject> findByOppervlaktegewicht(@Param("name") String name);
    List<Subject> findByThermischeGeleidbaarheidl(@Param("name") String name);
    List<Subject> findByThermischeGeleidbaarheidIsolatiel(@Param("name") String name);
    List<Subject> findByLineaireUitzettingsCoefficientTgvTemp(@Param("name") String name);
    List<Subject> findByLineaireUitzettingTempVWNL(@Param("name") String name);
    List<Subject> findByLineaireUitzettingTempVWFR(@Param("name") String name);
    List<Subject> findByWaterdampdiffusieWeerstandsgetalMu(@Param("name") String name);
    List<Subject> findByWaterdampdiffusieWeerstandsgetalIsolatieMu(@Param("name") String name);
    List<Subject> findByWaterdampdiffusieWeerstandsgetalMud(@Param("name") String name);
    List<Subject> findByLineaireUitzettingsCoefficientTgvRV(@Param("name") String name);
    List<Subject> findByLineaireUitzettingVochtVWNL(@Param("name") String name);
    List<Subject> findByLineaireUitzettingVochtVWFR(@Param("name") String name);
    List<Subject> findByBasishoeveelheidseenheid(@Param("name") String name);
//    List<Subject> findByEAN(@Param("name") String name);
//    List<Subject> findByEANEenheid(@Param("name") String name);
    List<Subject> findByIntrastatNummer(@Param("name") String name);
//    List<Subject> findByDOPURLNL(@Param("name") String name);
//    List<Subject> findByDOPURLFR(@Param("name") String name);
//    List<Subject> findByPIBURLNL(@Param("name") String name);
//    List<Subject> findByPIBURLFR(@Param("name") String name);
//    List<Subject> findByImage(@Param("name") String name);


}
