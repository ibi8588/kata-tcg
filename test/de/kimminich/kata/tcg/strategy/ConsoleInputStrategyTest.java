package de.kimminich.kata.tcg.strategy;

import de.kimminich.kata.tcg.Card;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import static de.kimminich.kata.tcg.syntactic.CardSugar.card;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

public class ConsoleInputStrategyTest {

    Strategy strategy;

    @Before
    public void setUp() {
        strategy = new ConsoleInputStrategy();
    }

    @Rule
    public TextFromStandardInputStream consoleInput = emptyStandardInputStream();

    @Test
    public void manualInputStrategyShouldPlayCardsSelectedOnSystemConsole() {
        player().enters("2").finished();

        assertThat(strategy.nextCard(10, Card.list(0, 2, 3)), is(card(2)));
    }

    @Test
    public void willRejectTooExpensiveCardsUntilAffordableCardIsChosen() {
        player().enters("8").enters("7").enters("6").finished();

        assertThat(strategy.nextCard(6, Card.list(6, 7, 8)), is(card(6)));
    }

    @Test
    public void willRejectCardsNotPresentInHandUntilHandCardIsChosen() {
        player().enters("1").enters("2").enters("3").finished();

        assertThat(strategy.nextCard(5, Card.list(3, 4, 5)), is(card(3)));
    }

    @Test
    public void willRejectInvalidInputUntilValidCardIsChosen() {
        player().enters("-1").enters("9").enters("666").enters("abc").enters("5").finished();

        assertThat(strategy.nextCard(10, Card.list(5)), is(card(5)));
    }

    private ConsoleInputBuilder player() {
        return new ConsoleInputBuilder();
    }

    private final class ConsoleInputBuilder {
        StringBuilder buf = new StringBuilder();

        public ConsoleInputBuilder enters(String in) {
            buf.append(in).append("\n");
            return this;
        }

        public void finished() {
            consoleInput.provideText(buf.toString());
        }
    }

}
