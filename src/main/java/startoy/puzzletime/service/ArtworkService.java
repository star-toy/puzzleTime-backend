package startoy.puzzletime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import startoy.puzzletime.dto.ArtworkDto;
import startoy.puzzletime.repository.ArtworkRepository;

@Service
public class ArtworkService {

    private ArtworkRepository artworkRepository;

    public ArtworkDto getArtworkByUid(String artworkUid) {
        return artworkRepository.findByArtworkUid(artworkUid)
                .map(artwork -> new ArtworkDto(
                        artwork.getArtworkUid(),
                        artwork.getArtworkTitle(),
                        artwork.getArtworkDescription()
                        //artwork.getTotalStages()
                ))
                .orElse(null);
    }
}
