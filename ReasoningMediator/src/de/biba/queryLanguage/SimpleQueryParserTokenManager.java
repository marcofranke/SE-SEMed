/* Generated By:JavaCC: Do not edit this line. SimpleQueryParserTokenManager.java */
package de.biba.queryLanguage;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import de.biba.mediator.CompareOperator;
import de.biba.mediator.IQuery;
import de.biba.mediator.InputQuery;
import de.biba.mediator.OutputQuery;
import de.biba.mediator.constraints.ClassConstraint;
import de.biba.mediator.constraints.Constraint;
import de.biba.mediator.constraints.ConstraintList;
import de.biba.mediator.constraints.OrConstraint;
import de.biba.mediator.constraints.PropertyConstraint;
import de.biba.mediator.constraints.SimplePropertyConstraint;
import de.biba.mediator.constraints.insert.ClassInsertConstraint;
import de.biba.mediator.constraints.insert.InsertConstraint;
import de.biba.mediator.constraints.insert.PropertyInsertConstraint;
import de.biba.mediator.constraints.insert.SimplePropertyInsertConstraint;
import de.biba.ontology.datatypes.BooleanDatatype;
import de.biba.ontology.datatypes.Datatype;
import de.biba.ontology.datatypes.NumericDatatype;
import de.biba.ontology.datatypes.StringDatatype;

/** Token Manager. */
public class SimpleQueryParserTokenManager implements SimpleQueryParserConstants
{

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_2(int pos, long active0)
{
   switch (pos)
   {
      default :
         return -1;
   }
}
private final int jjStartNfa_2(int pos, long active0)
{
   return jjMoveNfa_2(jjStopStringLiteralDfa_2(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_2()
{
   switch(curChar)
   {
      case 93:
         return jjStopAtPos(0, 45);
      default :
         return jjMoveNfa_2(0, 0);
   }
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_2(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 1;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  kind = 44;
                  jjstateSet[jjnewStateCnt++] = 0;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0xffffffffdfffffffL & l) == 0L)
                     break;
                  kind = 44;
                  jjstateSet[jjnewStateCnt++] = 0;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((jjbitVec0[i2] & l2) == 0L)
                     break;
                  if (kind > 44)
                     kind = 44;
                  jjstateSet[jjnewStateCnt++] = 0;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 1 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 9:
         jjmatchedKind = 3;
         return jjMoveNfa_0(2, 0);
      case 10:
         jjmatchedKind = 2;
         return jjMoveNfa_0(2, 0);
      case 32:
         jjmatchedKind = 1;
         return jjMoveNfa_0(2, 0);
      case 34:
         jjmatchedKind = 31;
         return jjMoveNfa_0(2, 0);
      case 35:
         jjmatchedKind = 21;
         return jjMoveNfa_0(2, 0);
      case 40:
         jjmatchedKind = 25;
         return jjMoveNfa_0(2, 0);
      case 41:
         jjmatchedKind = 26;
         return jjMoveNfa_0(2, 0);
      case 44:
         jjmatchedKind = 33;
         return jjMoveNfa_0(2, 0);
      case 45:
         jjmatchedKind = 22;
         return jjMoveNfa_0(2, 0);
      case 46:
         jjmatchedKind = 27;
         return jjMoveNfa_0(2, 0);
      case 47:
         jjmatchedKind = 24;
         return jjMoveNfa_0(2, 0);
      case 58:
         jjmatchedKind = 23;
         return jjMoveNfa_0(2, 0);
      case 59:
         jjmatchedKind = 34;
         return jjMoveNfa_0(2, 0);
      case 63:
         jjmatchedKind = 38;
         return jjMoveNfa_0(2, 0);
      case 65:
         jjmatchedKind = 18;
         return jjMoveStringLiteralDfa1_0(0x10000L);
      case 66:
         return jjMoveStringLiteralDfa1_0(0x40L);
      case 68:
         return jjMoveStringLiteralDfa1_0(0x4000L);
      case 70:
         return jjMoveStringLiteralDfa1_0(0x2000L);
      case 73:
         return jjMoveStringLiteralDfa1_0(0x800L);
      case 76:
         return jjMoveStringLiteralDfa1_0(0x80L);
      case 79:
         return jjMoveStringLiteralDfa1_0(0x9120L);
      case 83:
         return jjMoveStringLiteralDfa1_0(0x400L);
      case 87:
         return jjMoveStringLiteralDfa1_0(0x200L);
      case 91:
         jjmatchedKind = 41;
         return jjMoveNfa_0(2, 0);
      case 95:
         jjmatchedKind = 20;
         return jjMoveNfa_0(2, 0);
      case 97:
         jjmatchedKind = 18;
         return jjMoveStringLiteralDfa1_0(0x10000L);
      case 98:
         return jjMoveStringLiteralDfa1_0(0x40L);
      case 100:
         return jjMoveStringLiteralDfa1_0(0x4000L);
      case 102:
         return jjMoveStringLiteralDfa1_0(0x2000L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x800L);
      case 108:
         return jjMoveStringLiteralDfa1_0(0x80L);
      case 111:
         return jjMoveStringLiteralDfa1_0(0x9120L);
      case 115:
         return jjMoveStringLiteralDfa1_0(0x400L);
      case 119:
         return jjMoveStringLiteralDfa1_0(0x200L);
      case 123:
         jjmatchedKind = 39;
         return jjMoveNfa_0(2, 0);
      case 125:
         jjmatchedKind = 40;
         return jjMoveNfa_0(2, 0);
      default :
         return jjMoveNfa_0(2, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(2, 0);
   }
   switch(curChar)
   {
      case 65:
         return jjMoveStringLiteralDfa2_0(active0, 0x40L);
      case 69:
         return jjMoveStringLiteralDfa2_0(active0, 0x400L);
      case 70:
         return jjMoveStringLiteralDfa2_0(active0, 0x100L);
      case 72:
         return jjMoveStringLiteralDfa2_0(active0, 0x200L);
      case 73:
         return jjMoveStringLiteralDfa2_0(active0, 0x6080L);
      case 78:
         return jjMoveStringLiteralDfa2_0(active0, 0x10800L);
      case 80:
         return jjMoveStringLiteralDfa2_0(active0, 0x1000L);
      case 82:
         if ((active0 & 0x8000L) != 0L)
         {
            jjmatchedKind = 15;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0x20L);
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x40L);
      case 101:
         return jjMoveStringLiteralDfa2_0(active0, 0x400L);
      case 102:
         return jjMoveStringLiteralDfa2_0(active0, 0x100L);
      case 104:
         return jjMoveStringLiteralDfa2_0(active0, 0x200L);
      case 105:
         return jjMoveStringLiteralDfa2_0(active0, 0x6080L);
      case 110:
         return jjMoveStringLiteralDfa2_0(active0, 0x10800L);
      case 112:
         return jjMoveStringLiteralDfa2_0(active0, 0x1000L);
      case 114:
         if ((active0 & 0x8000L) != 0L)
         {
            jjmatchedKind = 15;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0x20L);
      default :
         break;
   }
   return jjMoveNfa_0(2, 1);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjMoveNfa_0(2, 1);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(2, 1);
   }
   switch(curChar)
   {
      case 68:
         if ((active0 & 0x10000L) != 0L)
         {
            jjmatchedKind = 16;
            jjmatchedPos = 2;
         }
         return jjMoveStringLiteralDfa3_0(active0, 0x20L);
      case 69:
         return jjMoveStringLiteralDfa3_0(active0, 0x200L);
      case 70:
         return jjMoveStringLiteralDfa3_0(active0, 0x100L);
      case 76:
         return jjMoveStringLiteralDfa3_0(active0, 0x2400L);
      case 77:
         return jjMoveStringLiteralDfa3_0(active0, 0x80L);
      case 83:
         return jjMoveStringLiteralDfa3_0(active0, 0x4840L);
      case 84:
         return jjMoveStringLiteralDfa3_0(active0, 0x1000L);
      case 100:
         if ((active0 & 0x10000L) != 0L)
         {
            jjmatchedKind = 16;
            jjmatchedPos = 2;
         }
         return jjMoveStringLiteralDfa3_0(active0, 0x20L);
      case 101:
         return jjMoveStringLiteralDfa3_0(active0, 0x200L);
      case 102:
         return jjMoveStringLiteralDfa3_0(active0, 0x100L);
      case 108:
         return jjMoveStringLiteralDfa3_0(active0, 0x2400L);
      case 109:
         return jjMoveStringLiteralDfa3_0(active0, 0x80L);
      case 115:
         return jjMoveStringLiteralDfa3_0(active0, 0x4840L);
      case 116:
         return jjMoveStringLiteralDfa3_0(active0, 0x1000L);
      default :
         break;
   }
   return jjMoveNfa_0(2, 2);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjMoveNfa_0(2, 2);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(2, 2);
   }
   switch(curChar)
   {
      case 69:
         if ((active0 & 0x40L) != 0L)
         {
            jjmatchedKind = 6;
            jjmatchedPos = 3;
         }
         return jjMoveStringLiteralDfa4_0(active0, 0xc20L);
      case 73:
         return jjMoveStringLiteralDfa4_0(active0, 0x1080L);
      case 82:
         return jjMoveStringLiteralDfa4_0(active0, 0x200L);
      case 83:
         return jjMoveStringLiteralDfa4_0(active0, 0x100L);
      case 84:
         return jjMoveStringLiteralDfa4_0(active0, 0x6000L);
      case 101:
         if ((active0 & 0x40L) != 0L)
         {
            jjmatchedKind = 6;
            jjmatchedPos = 3;
         }
         return jjMoveStringLiteralDfa4_0(active0, 0xc20L);
      case 105:
         return jjMoveStringLiteralDfa4_0(active0, 0x1080L);
      case 114:
         return jjMoveStringLiteralDfa4_0(active0, 0x200L);
      case 115:
         return jjMoveStringLiteralDfa4_0(active0, 0x100L);
      case 116:
         return jjMoveStringLiteralDfa4_0(active0, 0x6000L);
      default :
         break;
   }
   return jjMoveNfa_0(2, 3);
}
private int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjMoveNfa_0(2, 3);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(2, 3);
   }
   switch(curChar)
   {
      case 67:
         return jjMoveStringLiteralDfa5_0(active0, 0x400L);
      case 69:
         if ((active0 & 0x200L) != 0L)
         {
            jjmatchedKind = 9;
            jjmatchedPos = 4;
         }
         return jjMoveStringLiteralDfa5_0(active0, 0x2100L);
      case 73:
         return jjMoveStringLiteralDfa5_0(active0, 0x4000L);
      case 79:
         return jjMoveStringLiteralDfa5_0(active0, 0x1000L);
      case 82:
         return jjMoveStringLiteralDfa5_0(active0, 0x820L);
      case 84:
         if ((active0 & 0x80L) != 0L)
         {
            jjmatchedKind = 7;
            jjmatchedPos = 4;
         }
         break;
      case 99:
         return jjMoveStringLiteralDfa5_0(active0, 0x400L);
      case 101:
         if ((active0 & 0x200L) != 0L)
         {
            jjmatchedKind = 9;
            jjmatchedPos = 4;
         }
         return jjMoveStringLiteralDfa5_0(active0, 0x2100L);
      case 105:
         return jjMoveStringLiteralDfa5_0(active0, 0x4000L);
      case 111:
         return jjMoveStringLiteralDfa5_0(active0, 0x1000L);
      case 114:
         return jjMoveStringLiteralDfa5_0(active0, 0x820L);
      case 116:
         if ((active0 & 0x80L) != 0L)
         {
            jjmatchedKind = 7;
            jjmatchedPos = 4;
         }
         break;
      default :
         break;
   }
   return jjMoveNfa_0(2, 4);
}
private int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjMoveNfa_0(2, 4);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(2, 4);
   }
   switch(curChar)
   {
      case 66:
         return jjMoveStringLiteralDfa6_0(active0, 0x20L);
      case 78:
         return jjMoveStringLiteralDfa6_0(active0, 0x5000L);
      case 82:
         if ((active0 & 0x2000L) != 0L)
         {
            jjmatchedKind = 13;
            jjmatchedPos = 5;
         }
         break;
      case 84:
         if ((active0 & 0x100L) != 0L)
         {
            jjmatchedKind = 8;
            jjmatchedPos = 5;
         }
         else if ((active0 & 0x400L) != 0L)
         {
            jjmatchedKind = 10;
            jjmatchedPos = 5;
         }
         else if ((active0 & 0x800L) != 0L)
         {
            jjmatchedKind = 11;
            jjmatchedPos = 5;
         }
         break;
      case 98:
         return jjMoveStringLiteralDfa6_0(active0, 0x20L);
      case 110:
         return jjMoveStringLiteralDfa6_0(active0, 0x5000L);
      case 114:
         if ((active0 & 0x2000L) != 0L)
         {
            jjmatchedKind = 13;
            jjmatchedPos = 5;
         }
         break;
      case 116:
         if ((active0 & 0x100L) != 0L)
         {
            jjmatchedKind = 8;
            jjmatchedPos = 5;
         }
         else if ((active0 & 0x400L) != 0L)
         {
            jjmatchedKind = 10;
            jjmatchedPos = 5;
         }
         else if ((active0 & 0x800L) != 0L)
         {
            jjmatchedKind = 11;
            jjmatchedPos = 5;
         }
         break;
      default :
         break;
   }
   return jjMoveNfa_0(2, 5);
}
private int jjMoveStringLiteralDfa6_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjMoveNfa_0(2, 5);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(2, 5);
   }
   switch(curChar)
   {
      case 65:
         return jjMoveStringLiteralDfa7_0(active0, 0x1000L);
      case 67:
         return jjMoveStringLiteralDfa7_0(active0, 0x4000L);
      case 89:
         if ((active0 & 0x20L) != 0L)
         {
            jjmatchedKind = 5;
            jjmatchedPos = 6;
         }
         break;
      case 97:
         return jjMoveStringLiteralDfa7_0(active0, 0x1000L);
      case 99:
         return jjMoveStringLiteralDfa7_0(active0, 0x4000L);
      case 121:
         if ((active0 & 0x20L) != 0L)
         {
            jjmatchedKind = 5;
            jjmatchedPos = 6;
         }
         break;
      default :
         break;
   }
   return jjMoveNfa_0(2, 6);
}
private int jjMoveStringLiteralDfa7_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjMoveNfa_0(2, 6);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
   return jjMoveNfa_0(2, 6);
   }
   switch(curChar)
   {
      case 76:
         if ((active0 & 0x1000L) != 0L)
         {
            jjmatchedKind = 12;
            jjmatchedPos = 7;
         }
         break;
      case 84:
         if ((active0 & 0x4000L) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 7;
         }
         break;
      case 108:
         if ((active0 & 0x1000L) != 0L)
         {
            jjmatchedKind = 12;
            jjmatchedPos = 7;
         }
         break;
      case 116:
         if ((active0 & 0x4000L) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 7;
         }
         break;
      default :
         break;
   }
   return jjMoveNfa_0(2, 7);
}
private int jjMoveNfa_0(int startState, int curPos)
{
   int strKind = jjmatchedKind;
   int strPos = jjmatchedPos;
   int seenUpto;
   input_stream.backup(seenUpto = curPos + 1);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { throw new Error("Internal Error"); }
   curPos = 0;
   int startsAt = 0;
   jjnewStateCnt = 42;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 2:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 30)
                        kind = 30;
                     jjCheckNAddStates(0, 3);
                  }
                  else if ((0x5000000000000000L & l) != 0L)
                  {
                     if (kind > 28)
                        kind = 28;
                  }
                  else if (curChar == 61)
                     jjCheckNAdd(14);
                  else if (curChar == 33)
                     jjCheckNAdd(14);
                  else if (curChar == 63)
                     jjstateSet[jjnewStateCnt++] = 25;
                  if (curChar == 60)
                     jjstateSet[jjnewStateCnt++] = 21;
                  else if (curChar == 62)
                     jjCheckNAdd(14);
                  if (curChar == 60)
                     jjCheckNAdd(14);
                  break;
               case 14:
                  if (curChar == 61 && kind > 28)
                     kind = 28;
                  break;
               case 15:
                  if (curChar == 33)
                     jjCheckNAdd(14);
                  break;
               case 16:
                  if ((0x5000000000000000L & l) != 0L && kind > 28)
                     kind = 28;
                  break;
               case 17:
                  if (curChar == 60)
                     jjCheckNAdd(14);
                  break;
               case 18:
                  if (curChar == 62)
                     jjCheckNAdd(14);
                  break;
               case 19:
                  if (curChar == 61)
                     jjCheckNAdd(14);
                  break;
               case 20:
                  if (curChar == 60)
                     jjstateSet[jjnewStateCnt++] = 21;
                  break;
               case 22:
                  if ((0x7ffe00800000000L & l) != 0L)
                     jjAddStates(4, 5);
                  break;
               case 23:
                  if (curChar == 62 && kind > 32)
                     kind = 32;
                  break;
               case 24:
                  if (curChar == 63)
                     jjstateSet[jjnewStateCnt++] = 25;
                  break;
               case 26:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 35)
                     kind = 35;
                  jjstateSet[jjnewStateCnt++] = 26;
                  break;
               case 28:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 30)
                     kind = 30;
                  jjCheckNAddStates(0, 3);
                  break;
               case 29:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 30;
                  break;
               case 30:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 31;
                  break;
               case 31:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 32;
                  break;
               case 32:
                  if ((0xa00000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 33;
                  break;
               case 33:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 34;
                  break;
               case 34:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 35;
                  break;
               case 35:
                  if ((0xa00000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 36;
                  break;
               case 36:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 37;
                  break;
               case 37:
                  if ((0x3ff000000000000L & l) != 0L && kind > 19)
                     kind = 19;
                  break;
               case 38:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(38, 39);
                  break;
               case 39:
                  if (curChar == 46)
                     jjCheckNAdd(40);
                  break;
               case 40:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 29)
                     kind = 29;
                  jjCheckNAdd(40);
                  break;
               case 41:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 30)
                     kind = 30;
                  jjCheckNAdd(41);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 2:
                  if ((0x7fffffe07fffffeL & l) != 0L)
                  {
                     if (kind > 36)
                        kind = 36;
                  }
                  if ((0x4000000040L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 12;
                  else if ((0x10000000100000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 8;
                  else if ((0x1000000010L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 4;
                  else if ((0x200000002L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 0:
                  if ((0x800000008L & l) != 0L && kind > 4)
                     kind = 4;
                  break;
               case 1:
               case 3:
                  if ((0x8000000080000L & l) != 0L)
                     jjCheckNAdd(0);
                  break;
               case 4:
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 5:
                  if ((0x1000000010L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 6:
                  if ((0x2000000020L & l) != 0L && kind > 17)
                     kind = 17;
                  break;
               case 7:
                  if ((0x20000000200000L & l) != 0L)
                     jjCheckNAdd(6);
                  break;
               case 8:
                  if ((0x4000000040000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               case 9:
                  if ((0x10000000100000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 10:
                  if ((0x8000000080000L & l) != 0L)
                     jjCheckNAdd(6);
                  break;
               case 11:
                  if ((0x100000001000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 10;
                  break;
               case 12:
                  if ((0x200000002L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 11;
                  break;
               case 13:
                  if ((0x4000000040L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 12;
                  break;
               case 21:
                  if ((0x7fffffe07fffffeL & l) != 0L)
                     jjCheckNAddTwoStates(22, 23);
                  break;
               case 22:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                     jjCheckNAddTwoStates(22, 23);
                  break;
               case 25:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 35)
                     kind = 35;
                  jjCheckNAdd(26);
                  break;
               case 26:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 35)
                     kind = 35;
                  jjCheckNAdd(26);
                  break;
               case 27:
                  if ((0x7fffffe07fffffeL & l) != 0L && kind > 36)
                     kind = 36;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 42 - (jjnewStateCnt = startsAt)))
         break;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { break; }
   }
   if (jjmatchedPos > strPos)
      return curPos;

   int toRet = Math.max(curPos, seenUpto);

   if (curPos < toRet)
      for (i = toRet - Math.min(curPos, seenUpto); i-- > 0; )
         try { curChar = input_stream.readChar(); }
         catch(java.io.IOException e) { throw new Error("Internal Error : Please send a bug report."); }

   if (jjmatchedPos < strPos)
   {
      jjmatchedKind = strKind;
      jjmatchedPos = strPos;
   }
   else if (jjmatchedPos == strPos && jjmatchedKind > strKind)
      jjmatchedKind = strKind;

   return toRet;
}
private final int jjStopStringLiteralDfa_1(int pos, long active0)
{
   switch (pos)
   {
      default :
         return -1;
   }
}
private final int jjStartNfa_1(int pos, long active0)
{
   return jjMoveNfa_1(jjStopStringLiteralDfa_1(pos, active0), pos + 1);
}
private int jjMoveStringLiteralDfa0_1()
{
   switch(curChar)
   {
      case 34:
         return jjStopAtPos(0, 43);
      default :
         return jjMoveNfa_1(0, 0);
   }
}
private int jjMoveNfa_1(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 1;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0xfffffffbffffffffL & l) == 0L)
                     break;
                  kind = 42;
                  jjstateSet[jjnewStateCnt++] = 0;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  kind = 42;
                  jjstateSet[jjnewStateCnt++] = 0;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((jjbitVec0[i2] & l2) == 0L)
                     break;
                  if (kind > 42)
                     kind = 42;
                  jjstateSet[jjnewStateCnt++] = 0;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 1 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   29, 38, 39, 41, 22, 23, 
};

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, null, null, null, null, null, 
null, null, null, null, null, null, null, "\137", "\43", "\55", "\72", "\57", "\50", 
"\51", "\56", null, null, null, "\42", null, "\54", "\73", null, null, null, "\77", 
"\173", "\175", "\133", null, "\42", null, "\135", };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
   "STRING",
   "UNITSTATE",
};

/** Lex State array. */
public static final int[] jjnewLexState = {
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
   -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, -1, 0, -1, 0, 
};
static final long[] jjtoToken = {
   0x3ffffffffff1L, 
};
static final long[] jjtoSkip = {
   0xeL, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[42];
private final int[] jjstateSet = new int[84];
protected char curChar;
/** Constructor. */
public SimpleQueryParserTokenManager(SimpleCharStream stream){
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}

/** Constructor. */
public SimpleQueryParserTokenManager(SimpleCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 42; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
public void SwitchTo(int lexState)
{
   if (lexState >= 3 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   switch(curLexState)
   {
     case 0:
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_0();
       break;
     case 1:
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_1();
       break;
     case 2:
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_2();
       break;
   }
     if (jjmatchedKind != 0x7fffffff)
     {
        if (jjmatchedPos + 1 < curPos)
           input_stream.backup(curPos - jjmatchedPos - 1);
        if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
        {
           matchedToken = jjFillToken();
       if (jjnewLexState[jjmatchedKind] != -1)
         curLexState = jjnewLexState[jjmatchedKind];
           return matchedToken;
        }
        else
        {
         if (jjnewLexState[jjmatchedKind] != -1)
           curLexState = jjnewLexState[jjmatchedKind];
           continue EOFLoop;
        }
     }
     int error_line = input_stream.getEndLine();
     int error_column = input_stream.getEndColumn();
     String error_after = null;
     boolean EOFSeen = false;
     try { input_stream.readChar(); input_stream.backup(1); }
     catch (java.io.IOException e1) {
        EOFSeen = true;
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
        if (curChar == '\n' || curChar == '\r') {
           error_line++;
           error_column = 0;
        }
        else
           error_column++;
     }
     if (!EOFSeen) {
        input_stream.backup(1);
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
     }
     throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

}
