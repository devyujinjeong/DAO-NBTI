package com.dao.nbti.common.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    /* ModelMapper : 런타임 시 객체 간의 매핑을 자동화해주는 라이브러리로, 기본 설정만으로도 단순한 변환을 수행할 수 있음
     *    - 코드 작성량 감소 및 설정을 통해 다양한 케이스를 지원, 복잡한 객체 구조의 변환에도 유연하게 대처 가능
     *    - 런타임에 Reflection을 사용하므로 성능 오버헤드가 있을 수 있음, 복잡한 매핑 규칙은 추가 설정이 필요
     * */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                /* 필드명이 완전히 일치할 경우에만 매핑, productCode자리에 categoryCode값을 알아서 매핑해버리는 상황 방지 */
                .setMatchingStrategy(MatchingStrategies.STRICT)
                /* setter 메소드 미사용 시 ModelMapper가 private 필드에 접근 가능하도록 하는 설정 */
                .setFieldAccessLevel(
                        org.modelmapper.config.Configuration.AccessLevel.PRIVATE
                )
                .setFieldMatchingEnabled(true);
        return modelMapper;
    }
}
