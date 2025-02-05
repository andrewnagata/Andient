/*
 * Created on Dec 3, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package crawford_final2604;

import com.softsynth.jsyn.*;
/**************
** WARNING - this code automatically generated by Wire.
** The real source is probably a Wire patch.
** Do NOT edit this file unless you copy it to another directory and change the name.
** Otherwise it is likely to get clobbered the next time you
** export Java source code from Wire.
**
** Wire is available from: http://www.softsynth.com/wire/
*/
public class LangFilteredPulse extends SynthNote
{
    // Declare units and ports.
    ExponentialLag expLag;
    Filter_LowPass lowPass;
    ExponentialLag expLag2;
    ExponentialLag expLag3;
    PulseOscillator plsOsc;
    public SynthInput realFreq;
    public SynthInput cutoff;
    public SynthInput reson;
    public SynthInput rate;
    EnvelopePlayer envPlay;
    SynthEnvelope envelope;

    public LangFilteredPulse()
    {
        this( Synth.getSharedContext() );
    }
    public LangFilteredPulse( SynthContext synthContext )
    {
        super( synthContext );
        // Create unit generators.
        add( expLag = new ExponentialLag(synthContext) );
        add( lowPass = new Filter_LowPass(synthContext) );
        add( expLag2 = new ExponentialLag(synthContext) );
        add( expLag3 = new ExponentialLag(synthContext) );
        add( plsOsc = new PulseOscillator(synthContext) );
        add( envPlay = new EnvelopePlayer(synthContext) );
        double[] envelopeData = {
            5, 1.0,
			0.1, 0.8,
			0.01, 0.799,
            5, 0.0, 
			0.01, 0.0,
        };
        envelope = new SynthEnvelope( synthContext, envelopeData );
        envelopeData = null;
        envelope.setSustainLoop( 1, 1 );
        envelope.setReleaseLoop( -1, -1 );
        // Connect units and ports.

        addPort( amplitude = plsOsc.amplitude, "amplitudeSetting" );
        amplitude.setup( 0.0, 0.5, 1.0 );
        addPort( output = envPlay.output, "output" );
        expLag.halfLife.set( 0, 1.5 );
        expLag.current.set( 0, 3.9999053478240967 );
        expLag.output.connect( plsOsc.frequency);
        lowPass.amplitude.set( 0, 1.0 );
        lowPass.output.connect( envPlay.amplitude);
        expLag2.halfLife.set( 0, 1.57 );
        expLag2.current.set( 0, 9.999621391296387 );
        expLag2.output.connect( lowPass.Q);
        expLag3.halfLife.set( 0, 1.57 );
        expLag3.current.set( 0, 1999.9515380859375 );
        expLag3.output.connect( lowPass.frequency);
        plsOsc.phase.set( 0, -0.5367943644523621 );
        plsOsc.width.set( 0, 0.0 );
        plsOsc.output.connect( lowPass.input);

        addPort( realFreq = expLag.input, "realFreq" );
        realFreq.setup( 0.0, 4.0, 3000.0 );

        addPort( cutoff = expLag3.input, "cutoff" );
        cutoff.setup( 0.0, 2000.0, 15000.0 );

        addPort( reson = expLag2.input, "reson" );
        reson.setup( 0.0, 10.0, 50.0 );

        addPort( rate = envPlay.rate, "rate" );
        rate.setup( 0.0, 1.0, 2.0 );
    }
    
    public void setStage( int time, int stage )
    {
        switch( stage )
        {
        case 0:
            envPlay.envelopePort.clear( time );
            envPlay.envelopePort.queueOn( time, envelope );
            start( time );
            break;
        case 1:
            envPlay.envelopePort.queueOff( time, envelope );
            break;
        case 2: // added by hand
			envPlay.envelopePort.clear(time);
			envPlay.envelopePort.queue(time, envelope, envelope
					.getNumFrames() - 1, 1);
        default:
            break;
        }
    }
}
