package com.kjstudy.core.util.transfer;

public class FileCannotWriteException extends RuntimeException {
	FileCannotWriteException(){
		super("file cannot write");
	}
}
