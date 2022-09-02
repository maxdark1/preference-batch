package ca.homedepot.preference.dto;

import lombok.Data;

import java.util.List;

@Data
public class RegistrationResponse {
    private List<Response> registration;
}
