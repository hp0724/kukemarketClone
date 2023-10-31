package kukekyakya.kukemarket;

import kukekyakya.kukemarket.entity.member.Role;
import kukekyakya.kukemarket.entity.member.RoleType;
import kukekyakya.kukemarket.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
//활성 profile local 일때만 bean으로 등록
@Profile("local")
public class InitDB {
    private final RoleRepository roleRepository;

    //빈의 생성과 의존성 주입이 끝난뒤에 초기화
    @PostConstruct
    public void initDB(){
        log.info("initialize database");
        initRole();
    }
    private void initRole(){
        roleRepository.saveAll(
                List.of(RoleType.values()).stream().map(roleType -> new Role(roleType)).collect(Collectors.toList()));

    }
}
