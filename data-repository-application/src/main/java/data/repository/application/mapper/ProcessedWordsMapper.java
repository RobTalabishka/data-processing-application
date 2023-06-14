package data.repository.application.mapper;

import data.repository.application.model.ProcessedWordsDTO;
import data.repository.application.model.entity.ProcessedWordsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The interface Processed words mapper.
 */
@Mapper
public interface ProcessedWordsMapper {
    /**
     * The constant INSTANCE.
     */
    ProcessedWordsMapper INSTANCE =
            Mappers.getMapper(ProcessedWordsMapper.class);


    /**
     * Copy to dt os processed words dto.
     *
     * @param source the source
     * @return the processed words dto
     */
    ProcessedWordsDTO copyToDTOs(ProcessedWordsEntity source);

    /**
     * Copy to dt os list.
     *
     * @param source the source
     * @return the list
     */
    List<ProcessedWordsDTO> copyToDTOs(List<ProcessedWordsEntity> source);

    /**
     * Copy to entity processed words entity.
     *
     * @param source the source
     * @return the processed words entity
     */
    @Mapping(target = "id", ignore = true)
    ProcessedWordsEntity copyToEntity(ProcessedWordsDTO source);

    /**
     * Copy to entities list.
     *
     * @param source the source
     * @return the list
     */
    List<ProcessedWordsEntity> copyToEntities(List<ProcessedWordsDTO> source);

}
