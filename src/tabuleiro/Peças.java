package tabuleiro;

public class Peças {
	
	protected Posições posicao;
	private tabuleiro tabuleiro;
	public Peças(tabuleiro tabuleiro) {
			this.tabuleiro = tabuleiro;
			posicao=null;
	}
	protected tabuleiro getTabuleiro() {
		return tabuleiro;
	}


}
