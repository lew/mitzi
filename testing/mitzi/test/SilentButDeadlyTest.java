package mitzi.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import mitzi.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runner.RunWith;

@RunWith(Parameterized.class)
public class SilentButDeadlyTest {

	private GameState game_state = new GameState();
	private IMove expected_move;
	private IBrain brain;

	@Before
	public void setUp() throws Exception {
	}

	public SilentButDeadlyTest(final String sbd_fen, final IMove sbd_move) {
		game_state.setToFEN(sbd_fen);
		brain = new MitziBrain();
		brain.set(game_state);
		expected_move = sbd_move;
	}

	@Parameterized.Parameters
	public static List<Object[]> sbds() {
		return Arrays
				.asList(new Object[][] {
						{
								// 0 - sbd.001
								"1qr3k1/p2nbppp/bp2p3/3p4/3P4/1P2PNP1/P2Q1PBP/1N2R1K1 b - - 0 1",
								new Move("d2c7") },
						{
								// 1 - sbd.002
								"1r2r1k1/3bnppp/p2q4/2RPp3/4P3/6P1/2Q1NPBP/2R3K1 w - - 0 1",
								new Move("c5c7") },
						{
								// 2 - sbd.003
								"2b1k2r/2p2ppp/1qp4n/7B/1p2P3/5Q2/PPPr2PP/R2N1R1K b k - 0 1",
								new Move("e8g8") },
						{
								// 3 - sbd.004
								"2b5/1p4k1/p2R2P1/4Np2/1P3Pp1/1r6/5K2/8 w - - 0 1",
								new Move("d6d8") },
						{
								// 4 - sbd.005
								"2brr1k1/ppq2ppp/2pb1n2/8/3NP3/2P2P2/P1Q2BPP/1R1R1BK1 w - - 0 1",
								new Move("g2g3") },
						{
								// 5 - sbd.006
								"2kr2nr/1pp3pp/p1pb4/4p2b/4P1P1/5N1P/PPPN1P2/R1B1R1K1 b - - 0 1",
								new Move("h5f7") },
						{
								// 6 - sbd.007
								"2r1k2r/1p1qbppp/p3pn2/3pBb2/3P4/1QN1P3/PP2BPPP/2R2RK1 b k - 0 1",
								new Move("e8g8") },
						{
								// 7 - sbd.008
								"2r1r1k1/pbpp1npp/1p1b3q/3P4/4RN1P/1P4P1/PB1Q1PB1/2R3K1 w - - 0 1",
								new Move("c1e1") },
						{
								// 8 - sbd.009
								"2r2k2/r4p2/2b1p1p1/1p1p2Pp/3R1P1P/P1P5/1PB5/2K1R3 w - - 0 1",
								new Move("c1d2") },
						{
								// 9 - sbd.010
								"2r3k1/5pp1/1p2p1np/p1q5/P1P4P/1P1Q1NP1/5PK1/R7 w - - 0 1",
								new Move("a1d1") },
						{
								// 10 - sbd.011
								"2r3qk/p5p1/1n3p1p/4PQ2/8/3B4/5P1P/3R2K1 w - - 0 1",
								new Move("e5e6") },
						{
								// 11 - sbd.012
								"3b4/3k1pp1/p1pP2q1/1p2B2p/1P2P1P1/P2Q3P/4K3/8 w - - 0 1",
								new Move("d3f3") },
						{
								// 12 - sbd.014
								"3q1rk1/3rbppp/ppbppn2/1N6/2P1P3/BP6/P1B1QPPP/R3R1K1 w - - 0 1",
								new Move("b5d4") },
						{
								// 13 - sbd.015
								"3r1rk1/p1q4p/1pP1ppp1/2n1b1B1/2P5/6P1/P1Q2PBP/1R3RK1 w - - 0 1",
								new Move("g5h6") },
						{
								// 14 - sbd.016
								"3r2k1/2q2p1p/5bp1/p1pP4/PpQ5/1P3NP1/5PKP/3R4 b - - 0 1",
								new Move("c7d6") },
						{
								// 15 - sbd.017
								"3r2k1/p1q1npp1/3r1n1p/2p1p3/4P2B/P1P2Q1P/B4PP1/1R2R1K1 w - - 0 1",
								new Move("a2c4") }, });
	}

	@Test
	public void testSilentButDeadly() {
		IMove bestmove = brain.search(60000, 60000, 2000, true, null);
		assertEquals(expected_move, bestmove);
	}
}
