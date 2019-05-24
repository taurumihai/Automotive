package tauru.springframework.WebApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tauru.springframework.WebApp.entities.Automotive;
import tauru.springframework.WebApp.repositories.AutomotiveRepository;

import java.util.List;

@Service
public class AutomotiveService {


    @Autowired
    private AutomotiveRepository automotiveRepository;


    public void saveAutomotive(Automotive automotive) {

        if (automotive != null) {

            automotiveRepository.save(automotive);
        }
    }

    public List<Automotive> getAllAutomotives() {

        return automotiveRepository.findAll();
    }

}
