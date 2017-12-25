/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractal.semantics;

import java.util.*;
import fractal.syntax.ASTDefine;
import fractal.syntax.ASTExpAdd;
import fractal.syntax.ASTExpDiv;
import fractal.syntax.ASTExpLit;
import fractal.syntax.ASTExpMod;
import fractal.syntax.ASTExpMul;
import fractal.syntax.ASTExpSub;
import fractal.syntax.ASTExpVar;
import fractal.syntax.ASTFracCompose;
import fractal.syntax.ASTFracInvocation;
import fractal.syntax.ASTFracSequence;
import fractal.syntax.ASTFracVar;
import fractal.syntax.ASTFractal;
import fractal.syntax.ASTRender;
import fractal.syntax.ASTRepeat;
import fractal.syntax.ASTRestoreStmt;
import fractal.syntax.ASTSaveStmt;
import fractal.syntax.ASTSelf;
import fractal.syntax.ASTStatement;
import fractal.syntax.ASTStmtSequence;
import fractal.syntax.ASTTCmdBack;
import fractal.syntax.ASTTCmdClear;
import fractal.syntax.ASTTCmdForward;
import fractal.syntax.ASTTCmdHome;
import fractal.syntax.ASTTCmdLeft;
import fractal.syntax.ASTTCmdPenDown;
import fractal.syntax.ASTTCmdPenUp;
import fractal.syntax.ASTTCmdRight;
import fractal.sys.FractalException;
import fractal.values.FractalValue;
import fractal.values.Fractal;
import fractal.values.PrimitiveFractal;
import cs34q.turtle.Turtle;
import cs34q.turtle.TurtleDisplay;
import fractal.values.Fractal;
import fractal.values.FractalValue;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author newts
 */
public class FractalEvaluator extends AbstractFractalEvaluator {
	
	protected FractalValue result;
	protected Fractal fractal;
	
	public FractalEvaluator() {
	// perform initialisations here
	result = FractalValue.NO_VALUE;
	fractal = null;
    }
	
    @Override
    public FractalValue visitASTStmtSequence(ASTStmtSequence seq, FractalState state) throws FractalException {
		ASTStatement s;
		ArrayList<ASTStatement> sseq = seq.getSeq();
		//ArrayList<ASTStatement> stmts
		Iterator iter = sseq.iterator();
		//for (ASTStatement stmt : seq) {
		while(iter.hasNext()) {
			//s = ASTStatement) stmt
			s = (ASTStatement) iter.next();
			result = s.visit(this, state);
		}
		// return last value evaluated
		return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
    }

    @Override
    public FractalValue visitASTSaveStmt(ASTSaveStmt form, FractalState state) throws FractalException {
        result = form.visit(this, state);
        return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTRestoreStmt(ASTRestoreStmt form, FractalState state) throws FractalException {
        result = form.visit(this, state);
        return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTRender(ASTRender form, FractalState state) throws FractalException {
        FractalValue level, scale, fractals;
        //Environment env = state.getEnvironment();
        level = form.getLevel().visit(this, state);
		scale = form.getScale().visit(this, state);
		fractals = form.getFractal().visit(this, state);
		invokeFractal(fractals.fractalValue(),new FractalState(state,fractals.fractalValue(),level.intValue(),scale.realValue()));
		//state.updateDisplay();
		return result;//fractal;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTDefine(ASTDefine form, FractalState state) throws FractalException {
		Environment env = state.getEnvironment();
		result = form.getValueExp().visit(this, state);
		env.put(form.getVar(), result);
		return result;

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }       
   
    
    @Override
    public FractalValue visitASTRepeat(ASTRepeat form, FractalState state) throws FractalException {
		Environment env = state.getEnvironment();
		String lv = form.getLoopVar();
		int count = form.getCountExp().visit(this, state).intValue();
		//FractalValue fracCount = form.getCountExp().visit(this, state);
		FractalValue fracCount = FractalValue.make(1);
		//env.put(lv,fracCount);
		for (int x = 0; x < count; x++){
			env.put(lv,fracCount);
			result = form.getBody().visit(this, state);
			fracCount = FractalValue.make(fracCount.intValue() + 1);
			
		}
		return result;
		
		
		//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTFracInvocation(ASTFracInvocation form, FractalState state) throws FractalException {
		fractal = form.getFractal();
		return fractal;
		//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTFracSequence(ASTFracSequence form, FractalState state) throws FractalException {
	// Create and return an instance of SequencedFractal here
        FractalValue val1, val2;
		val1 = form.getFirst().visit(this, state);
		val2 = form.getSecond().visit(this, state);
		return val1.add(val2);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTFracCompose(ASTFracCompose form, FractalState state) throws FractalException {
	// Create and return an instance of CompositeFractal here
        FractalValue val1, val2;
		val1 = form.getFirst().visit(this, state);
		val2 = form.getSecond().visit(this, state);
		return val1.add(val2);
		//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTFracVar(ASTFracVar form, FractalState state) throws FractalException {
        Environment env = state.getEnvironment();
        result = env.get(form.getVar());
        return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTFractal(ASTFractal form, FractalState state) throws FractalException {
	// Create and return an instance of a PrimitiveFractal here
        ArrayList<ASTStatement> body = form.getBody();
        PrimitiveFractal one = new PrimitiveFractal(state.getCurrentScale(),body,state);
        //result = body.visit(this,state);
        return one;//body;//result;
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTSelf(ASTSelf form, FractalState state) throws FractalException {
        int level = state.getCurrentLevel();
		double scale = state.getCurrentScale();
        FractalValue fractals = state.getCurrentFractal();
        invokeFractal(fractals.fractalValue(),new FractalState(state,fractals.fractalValue(),level,scale));
        //invokeFractal(null,new FractalState(state,null,1,1));
		return result;
        //return form.getLength().visit(this,state);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdLeft(ASTTCmdLeft form, FractalState state) throws FractalException {
       FractalValue angle = form.getAngle().visit(this,state);
       //deriveTurned(state,angle.realValue());
       turnTurtle(state,angle.realValue());
       return result;
        //return form.getAngle().visit(this,state);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdRight(ASTTCmdRight form, FractalState state) throws FractalException {
       FractalValue angle = form.getAngle().visit(this,state);
       turnTurtle(state,(-angle.realValue()));
       //state.deriveTurned(angle.realValue());
       return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdForward(ASTTCmdForward form, FractalState state) throws FractalException {
       FractalValue length = form.getLength().visit(this,state);
       displaceTurtle(state,length.realValue());
       return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdBack(ASTTCmdBack form, FractalState state) throws FractalException {
       FractalValue length = form.getLength().visit(this,state);
       displaceTurtle(state,(-length.realValue()));
       return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdPenDown(ASTTCmdPenDown form, FractalState state) throws FractalException {
        //setTurtleDown(state,state.getTurtleState());
        boolean bool = true;
        state.getTurtleState().setPenDown(bool);
        return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdPenUp(ASTTCmdPenUp form, FractalState state) throws FractalException {
        boolean bool = false;
        state.getTurtleState().setPenDown(bool);
        return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdClear(ASTTCmdClear form, FractalState state) throws FractalException {
        state.getDisplay().clear();
        return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTTCmdHome(ASTTCmdHome form, FractalState state) throws FractalException {
        state.getTurtleState().home();
        return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTExpAdd(ASTExpAdd form, FractalState state) throws FractalException {
        FractalValue val1, val2;
		val1 = form.getFirst().visit(this, state);
		val2 = form.getSecond().visit(this, state);
		return FractalValue.make(val1.intValue() + val2.intValue());
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTExpSub(ASTExpSub form, FractalState state) throws FractalException {
        FractalValue val1, val2;
		val1 = form.getFirst().visit(this, state);
		val2 = form.getSecond().visit(this, state);
		return FractalValue.make(val1.intValue() - val2.intValue());
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTExpMul(ASTExpMul form, FractalState state) throws FractalException {
        FractalValue val1, val2;
		val1 = form.getFirst().visit(this, state);
		val2 = form.getSecond().visit(this, state);
		return FractalValue.make(val1.intValue() * val2.intValue());
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTExpDiv(ASTExpDiv form, FractalState state) throws FractalException {
        FractalValue val1, val2;
		val1 = form.getFirst().visit(this, state);
		val2 = form.getSecond().visit(this, state);
		return FractalValue.make(val1.intValue() + val2.intValue());
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTExpMod(ASTExpMod form, FractalState state) throws FractalException {
        FractalValue val1, val2;
		val1 = form.getFirst().visit(this, state);
		val2 = form.getSecond().visit(this, state);
		return FractalValue.make(val1.intValue() % val2.intValue());
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTExpLit(ASTExpLit form, FractalState state) throws FractalException {
        result = form.getValue();
        return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTExpVar(ASTExpVar form, FractalState state) throws FractalException {
        Environment env = state.getEnvironment();;
		result = env.get(form.getVar());
		return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
