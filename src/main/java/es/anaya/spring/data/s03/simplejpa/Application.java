package es.anaya.spring.data.s03.simplejpa;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Log4j
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    DataSource dataSource;

    @Autowired
    TechnologyRepository technologyRepository;

    @Autowired
    CsvWriter csvWriter;

    @PersistenceContext
    EntityManager entityManager;

    public static final String ORDEN_TRABAJO_FICHERO_URL="ficheros/csvs";

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @Transactional(readOnly = true)
    @Override
    public void run(String... args) throws Exception {

        System.out.println("DATASOURCE = " + dataSource);
/*
        System.out.println("Showing all records");
        for (Technology technology : technologyRepository.findAll()) {
            System.out.println(technology);
        }

        System.out.println("Select by creation date");
        for (Technology technology : technologyRepository.findByCreationYear(1998)) {
            System.out.println(technology);
        }

        System.out.println("Select by name with stream:");
        technologyRepository
                .findByNameReturnStream("%Java%")
                .forEach(tech -> System.out.println(tech));

*/
        //Consulta para recoger las cabeceras
        List<Tuple> result = entityManager.createNativeQuery("SELECT * FROM technology", Tuple.class).getResultList();

        //Cabeceras
        List<String> cabeceras = new ArrayList<>();
        List<TupleElement<?>> elements = result.get(0).getElements();
        for (TupleElement<?> element: elements){
            cabeceras.add(element.getAlias());
        }

        List<Object> registros = entityManager.createNativeQuery("SELECT * FROM technology").getResultList();


        String path = creteOrderFolder(1695L).toString();
        csvWriter.createCsv(registros, cabeceras, path);

    }


    public File creteOrderFolder(Long ordenId){
        File file = new File(ORDEN_TRABAJO_FICHERO_URL + "/" + ordenId);
        if(!file.exists()){
            if(!file.mkdir())
            System.out.println("Nose pudo crear el fichero");        }
        return file;
    }
}
