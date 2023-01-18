package br.unb.garage_relation.factories;

import br.unb.garage_relation.model.User;
import br.unb.garage_relation.model.dto.request.CarCreateDTO;

public class CarFactory {
	public static CarCreateDTO createCarCreateDTO(User owner) {
		return new CarCreateDTO(
				"Uno",
				"Fiat",
				2010,
				owner.getId(),
				owner
		);
	}
	

}
