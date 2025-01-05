package startoy.puzzletime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import startoy.puzzletime.domain.Artwork;
import startoy.puzzletime.dto.artwork.CompleteArtworksResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

    // UID 로 Artwork 조회
    Optional<Artwork> findByArtworkUid(String artworkUid);

    // 특정 ThemeId로 Artwork 리스트 조회
    List<Artwork> findByThemeThemeId(Long themeId);

    // userId로 완성한 artwork과 reward 조회
    @Query(value = """
            SELECT ta.artwork_seq as artworkSeq, ta.artwork_uid as artworkUid, atis.image_url as artworkImgUrl, ta.reward_code as rewardCode, rtis.image_url as rewardImgUrl
              FROM tb_user_artworks tua
              join tb_artworks ta
                on tua.artwork_id = ta.artwork_id
              join tb_image_storage atis
                on ta.artwork_image_id = atis.image_id
              join tb_image_storage rtis
                on ta.gallery_reward_image_id = rtis.image_id -- 갤러리용 reward 조회를 위함
             WHERE tua.user_id = :userId
               and tua.is_completed = '1'
            """, nativeQuery = true)
    List<Map<String, Object>> findCompleteArtworks(@Param("userId") Long userId);
}
