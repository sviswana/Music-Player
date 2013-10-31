package player;
/**
 * This class handles meter from the field M: in the header
 *
 */
public class Meter implements HeaderField{
	private final Fraction meter;
	
	/**
	 * Initializes the meter with the specified meter
	 * @param meter Fraction
	 */
	public Meter(Fraction meter){
		this.meter = meter;
	}
	
	/**
	 * The meter of the music. eg. 4/4
	 * @return Fraction, representing the meter
	 */
	public Fraction getMeter(){
		return meter;
	}
	
	/**
	 * Returns the meter in easy-to-read format.
	 * @return String, representing the meter of the music
	 */
	@Override
	public String toString(){
	    return "Meter: " + meter.toString();
	            
	}
}
