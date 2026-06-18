package com.undec.museobackend.persistence;

import com.undec.museobackend.model.RefreshToken;
import com.undec.museobackend.output.RefreshTokenRepositoryPort;
import com.undec.museobackend.valueobjects.UserId;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {

    private final RefreshTokenJpaRepository jpaRepository;

    public RefreshTokenRepositoryAdapter(RefreshTokenJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenJpaEntity entity = toEntity(refreshToken);
        jpaRepository.save(entity);
        return refreshToken;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpaRepository.findByToken(token).map(this::toDomain);
    }

    @Override
    @Transactional
    public void revokeAllByUserId(UserId userId) {
        jpaRepository.revokeAllByUserId(userId.getValue());
    }

    private RefreshTokenJpaEntity toEntity(RefreshToken domain) {
        return new RefreshTokenJpaEntity(
                domain.getId(),
                domain.getToken(),
                domain.getUserId().getValue(),
                domain.getExpirationDate(),
                domain.isRevoked()
        );
    }

    private RefreshToken toDomain(RefreshTokenJpaEntity entity) {
        return RefreshToken.reconstitute(
                entity.getId(),
                entity.getToken(),
                UserId.of(entity.getUserId()),
                entity.getExpirationDate(),
                entity.isRevoked()
        );
    }
}
