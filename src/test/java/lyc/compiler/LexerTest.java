package lyc.compiler;

import lyc.compiler.factories.LexerFactory;
import lyc.compiler.model.CompilerException;
import lyc.compiler.model.InvalidIntegerException;
import lyc.compiler.model.InvalidLengthException;
import lyc.compiler.model.UnknownCharacterException;
import lyc.compiler.model.FloatOutOfRangeException;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;
import static lyc.compiler.constants.Constants.MAX_LENGTH;
import static org.junit.jupiter.api.Assertions.assertThrows;


//@Disabled
public class LexerTest {

  private Lexer lexer;


  @Test
  public void comment() throws Exception{
    scan("*-This is a comment-*");
    assertThat(nextToken()).isEqualTo(ParserSym.EOF);
  }
  @Test
  public void emptycomment() throws Exception{
    scan("*--*");
    assertThat(nextToken()).isEqualTo(ParserSym.EOF);
  }


  @Test
  public void validStringConstantLength() throws Exception{
    
    scan("\"algoaaaaaaaaaaaalgoaaaaaaaaaaaaaaaaaaaaa\"");
    assertThat(nextToken()).isEqualTo(ParserSym.STRING_CONSTANT);
  }
  @Test
  public void validemptyStringConstantLength() throws Exception{
    
    scan("\"\"");
    assertThat(nextToken()).isEqualTo(ParserSym.STRING_CONSTANT);
  }

  @Test
  public void invalidStringConstantLength() {
    assertThrows(InvalidLengthException.class, () -> {
      scan("\"%s\"".formatted(getRandomString()));
      nextToken();
    });
  }


  @Test
  public void validId() throws Exception{
    scan("nombreVar1");
    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
  }

  @Test
  public void invalidIdLength() {
    assertThrows(InvalidLengthException.class, () -> {
      scan(getRandomString());
      nextToken();
    });
  }

  @Test
  public void validFloat1() throws Exception{
    scan("3.56");
    assertThat(nextToken()).isEqualTo(ParserSym.FLOAT_CONSTANT);
  }
  @Test
  public void validFloat2() throws Exception{
    scan(".234");
    assertThat(nextToken()).isEqualTo(ParserSym.FLOAT_CONSTANT);
  }
  @Test
  public void validFloat3() throws Exception{
    scan("2453.");
    assertThat(nextToken()).isEqualTo(ParserSym.FLOAT_CONSTANT);
  }
 @Test
  public void invalidFloat1() {
    assertThrows(FloatOutOfRangeException.class, () -> {
      scan("355863945743249587624976234976398472982795.34545");
      nextToken();
    });
  }
  // @Test
  // public void palabrasReservadas() throws Exception{
  //   scan("¿ ¿¿y? ?o?decimal ??sino no es?? 2init entero si rora mientras");
  //   assertThat(nextToken()).isEqualTo(ParserSym.START_IF);
  //   assertThat(nextToken()).isEqualTo(ParserSym.START_WHILE);
  //   assertThat(nextToken()).isEqualTo(ParserSym.AND);
  //   assertThat(nextToken()).isEqualTo(ParserSym.END_IF);
  //   assertThat(nextToken()).isEqualTo(ParserSym.END_IF);
  //   assertThat(nextToken()).isEqualTo(ParserSym.OR);
  //   assertThat(nextToken()).isEqualTo(ParserSym.END_IF);
  //   assertThat(nextToken()).isEqualTo(ParserSym.TYPE_FLOAT);
  //   assertThat(nextToken()).isEqualTo(ParserSym.END_WHILE);
  //   assertThat(nextToken()).isEqualTo(ParserSym.ELSE);
  //   assertThat(nextToken()).isEqualTo(ParserSym.NOT);
  //   assertThat(nextToken()).isEqualTo(ParserSym.END_WHILE);
  //   assertThat(nextToken()).isEqualTo(ParserSym.INTEGER_CONSTANT);
  //   assertThat(nextToken()).isEqualTo(ParserSym.INIT);
  //   assertThat(nextToken()).isEqualTo(ParserSym.TYPE_INTEGER);
  //   assertThat(nextToken()).isEqualTo(ParserSym.IF);
  //   assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
  //   assertThat(nextToken()).isEqualTo(ParserSym.WHILE);
  // }
  @Test
  public void otrasPalabrasReservadas() throws Exception{
    scan("si y sino o no es init texto mientras *-sff-* decimal getPenultimatePosition triangulo");
    assertThat(nextToken()).isEqualTo(ParserSym.IF);
    assertThat(nextToken()).isEqualTo(ParserSym.AND);
    assertThat(nextToken()).isEqualTo(ParserSym.ELSE);
    assertThat(nextToken()).isEqualTo(ParserSym.OR);
    assertThat(nextToken()).isEqualTo(ParserSym.NOT);
    assertThat(nextToken()).isEqualTo(ParserSym.INIT);
    assertThat(nextToken()).isEqualTo(ParserSym.TYPE_STRING);
    assertThat(nextToken()).isEqualTo(ParserSym.WHILE);
    assertThat(nextToken()).isEqualTo(ParserSym.TYPE_FLOAT);
    assertThat(nextToken()).isEqualTo(ParserSym.GETPENULTIMATEPOSITION);
    assertThat(nextToken()).isEqualTo(ParserSym.TRIANGULO);
  }
  // @Test
  // public void invalidPositiveIntegerConstantValue() {
  //   assertThrows(InvalidIntegerException.class, () -> {
  //     scan("%d".formatted(9223372036854775807L));
  //     nextToken();
  //   });
  // }

  // @Test
  // public void invalidNegativeIntegerConstantValue() {
  //   assertThrows(InvalidIntegerException.class, () -> {
  //     scan("%d".formatted(-9223372036854775807L));
  //     nextToken();
  //   });
  // }


  @Test
  public void assignmentWithExpressions() throws Exception {
    scan("c=d*(e-21)/4");
    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.ASSIG);
    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.MULT);
    assertThat(nextToken()).isEqualTo(ParserSym.OPEN_BRACKET);
    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.SUB);
    assertThat(nextToken()).isEqualTo(ParserSym.INTEGER_CONSTANT);
    assertThat(nextToken()).isEqualTo(ParserSym.CLOSE_BRACKET);
    assertThat(nextToken()).isEqualTo(ParserSym.DIV);
    assertThat(nextToken()).isEqualTo(ParserSym.INTEGER_CONSTANT);
    assertThat(nextToken()).isEqualTo(ParserSym.EOF);
  }

  // @Test
  // public void unknownCharacter() {
  //   assertThrows(UnknownCharacterException.class, () -> {
  //     scan("#");
  //     nextToken();
  //   });
  // }

  @AfterEach
  public void resetLexer() {
    lexer = null;
  }

  private void scan(String input) {
    lexer = LexerFactory.create(input);
  }

  private int nextToken() throws IOException, CompilerException {
    return lexer.next_token().sym;
  }

  private static String getRandomString() {
    return new RandomStringGenerator.Builder()
            .filteredBy(CharacterPredicates.LETTERS)
            .withinRange('a', 'z')
            .build().generate(MAX_LENGTH * 2);
  }

}
