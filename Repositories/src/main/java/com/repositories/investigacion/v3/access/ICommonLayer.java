package com.repositories.investigacion.v3.access;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.repositories.investigacion.v3.utilities.dto.Entry;

public interface ICommonLayer {

	public ResponseEntity<List<Entry>> accessRepoService(String query);
}
