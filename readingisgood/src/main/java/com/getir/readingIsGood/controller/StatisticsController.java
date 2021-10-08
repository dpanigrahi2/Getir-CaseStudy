package com.getir.readingIsGood.controller;

import com.getir.readingIsGood.model.Statistics;
import com.getir.readingIsGood.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
@Api(tags = {"Customer Statistics"})
public class StatisticsController
{
	private final StatisticsService statisticsService;

	@GetMapping("/{customerId}")
	//@ApiOperation("Get customer's statistics")
	public ResponseEntity<Object> getStatistics(@ApiParam(name = "customerId") @PathVariable("customerId") String customerId)
	{
		Collection<Statistics> statistics = statisticsService.getStatistics(customerId);
		return new ResponseEntity<>(statistics, HttpStatus.OK);
	}

}
