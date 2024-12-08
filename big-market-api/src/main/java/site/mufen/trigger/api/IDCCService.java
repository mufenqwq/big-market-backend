package site.mufen.trigger.api;


import site.mufen.types.model.Response;

public interface IDCCService {

    Response<Boolean> updateConfig(String key, String value);
}
