package ru.primer.primeapi.model;

import java.util.List;

public record ProxyUser(Integer id, String name, String joinDate, String lang,
                        String password, Integer rubles, String registerIp,
                        String lastIp, Boolean privateMessagesToggle,
                        List<String> privateMessagesIgnore) {

}