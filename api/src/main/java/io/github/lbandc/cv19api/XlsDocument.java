package io.github.lbandc.cv19api;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
class XlsDocument {

	void processFile(File file) throws IOException, InvalidFormatException {
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		log.debug(String.valueOf(workbook.getActiveSheetIndex()));
		XSSFSheet sheet = workbook.getSheetAt(0);
		for (Row row : sheet) {
			log.info(row.toString());
		}
		workbook.close();

	}
}
