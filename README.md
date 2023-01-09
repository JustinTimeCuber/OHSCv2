# OHSCv2
Scoreboard for the card game Oh Hell, made with Processing.

# Game Description

## Bidding
In Oh Hell, a number of cards is dealt to all players and a "trump" suit is chosen (generally from the top of the deck). Players then proceed to "bid" (from the left of the dealer) on how many "tricks" (rounds of each person playing one card) they will take throughout the hand. The dealer (who is the last bidder) is not allowed to make the total bid by all players equal the number of tricks available. That is, if 9 cards are dealt (9 tricks are available), and the total bid prior to the dealer is 7, the dealer must not bid 2, but may bid 0, 1, 3, etc.

## Playing
After everyone has recorded their bid, play begins starting with the person to the left of the dealer. Everyone must follow suit unless they do not have that suit, and the winner of the trick is whoever played the highest card of that suit (aces are high), or if the trump suit was played, whoever has the highest trump. Following suit also applies for trump, so if trump is led, then everyone who has it must play it. Play continues until all cards have been played, with the person who wins each trick starting (or "leading") the next one.

## Scoring
After a full hand is played, each player is scored based on their bid and their tricks taken. If they took too few tricks, they simply get 1 point for each trick taken; for example, if a player bids 3 and takes 2, they receive 2 points. If they took too many tricks, they lose a flat 10 points (negative scores are possible). This is regardless of how many extra tricks are taken, so a player who bids 1 and takes 4 would still lose 10 points (not 30). If a player exactly matches their bid, they gain one point for each trick plus a 10 point bonus. So, a player who bids 3 and takes 3 would get 13 points. Bidding zero is allowed; if a player bids 0 and takes 0, they would gain 10 points.

# Scoreboard Software Description
This scoreboard is written in Processing, which is based on Java. Currently, you'll need to [install Processing](https://processing.org/download) to run the program, but in the future I intend to upload compiled JAR files.

There are two main screens within the program: setup mode, and scoreboard mode.

## Setup mode
![image](https://user-images.githubusercontent.com/46458276/211226034-efe91654-1b0c-4252-a4c4-6f00e80f657f.png)
Setup mode is used for entering player names, setting the trick mode, and setting a custom deck size (e.g. extra suits) as well as manually changing scores during the game.

## Scoreboard mode
![image](https://user-images.githubusercontent.com/46458276/211226050-610ec5b3-7a44-48c5-88fc-53f7cbd32437.png)
Scoreboard mode is used for showing the score while playing the game. It allows for entering bids and tricks taken, and it automatically calculates the score after each round.
