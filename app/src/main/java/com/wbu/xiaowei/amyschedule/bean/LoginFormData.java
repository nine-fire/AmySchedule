package com.wbu.xiaowei.amyschedule.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录教务系统时必须的表单数据
 */
public class LoginFormData {
    private String username;
    private String password;
    private String lt;
    private String execution;
    private String _eventId;
    private String rmShown;

    public Map<String, String> toRequestParameters() {
        Map<String, String> params = new HashMap<>();

        params.put("username", username);
        params.put("password", password);
        params.put("lt", lt);
        params.put("execution", execution);
        params.put("_eventId", _eventId);
        params.put("rmShown", rmShown);

        return params;
    }

    public boolean verify() {
        return lt != null && execution != null && _eventId != null && rmShown != null;
    }

    public boolean isEmpty() {
        if (lt != null) {
            return true;
        }
        if (execution != null) {
            return true;
        }
        if (_eventId != null) {
            return true;
        }
        if (rmShown != null) {
            return true;
        }
        return false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLt() {
        return lt;
    }

    public void setLt(String lt) {
        this.lt = lt;
    }

    public String getExecution() {
        return execution;
    }

    public void setExecution(String execution) {
        this.execution = execution;
    }

    public String get_eventId() {
        return _eventId;
    }

    public void set_eventId(String _eventId) {
        this._eventId = _eventId;
    }

    public String getRmShown() {
        return rmShown;
    }

    public void setRmShown(String rmShown) {
        this.rmShown = rmShown;
    }

    public LoginFormData(String username, String password, String lt, String execution, String _eventId, String rmShown) {
        this.username = username;
        this.password = password;
        this.lt = lt;
        this.execution = execution;
        this._eventId = _eventId;
        this.rmShown = rmShown;
    }

    public LoginFormData() {
    }

    @Override
    public String toString() {
        return "LoginFormData{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", lt='" + lt + '\'' +
                ", execution='" + execution + '\'' +
                ", _eventId='" + _eventId + '\'' +
                ", rmShown='" + rmShown + '\'' +
                '}';
    }
}
