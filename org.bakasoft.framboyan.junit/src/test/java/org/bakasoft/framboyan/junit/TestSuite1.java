package org.bakasoft.framboyan.junit;

import org.bakasoft.framboyan.Framboyan;

public class TestSuite1 extends Framboyan {{

	describe("Describe 1", () -> {


		describe("Describe 1.1", () -> {
			it("It 1", () -> {
				// pass
			});
			it("It 2", () -> {
				// pass
			});
			it("It 3", () -> {
				// pass
			});
		});


		describe("Describe 1.2", () -> {
			it("It 1", () -> {
				// pass
			});
			it("It 2", () -> {
				// pass
			});
			it("It 3", () -> {
				// pass
			});
		});
		
		it("It 1", () -> {
			// pass
		});
		xit("It 2", () -> {
			// pass
		});
		it("It 3", () -> {
			// pass
		});
	});

	xdescribe("Describe 2", () -> {
		it("It 1", () -> {
			// pass
		});
		it("It 2", () -> {
			// pass
		});
		it("It 3", () -> {
			// pass
		});
	});

	describe("Describe 3", () -> {
		it("It 1", () -> {
			// pass
		});
		it("It 2", () -> {
			fail();
		});
		it("It 3", () -> {
			// pass
		});
	});
	
}}
