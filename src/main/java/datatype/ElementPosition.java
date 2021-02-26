package datatype;

import java.io.Serializable;

public class ElementPosition<SlotNumber, ElementNumber> implements Serializable
{

    private SlotNumber slotNumber;
    private ElementNumber elementNumber;

    public ElementPosition(SlotNumber slotNumber, ElementNumber elementNumber){
        this.slotNumber = slotNumber;
        this.elementNumber = elementNumber;
    }
    
    // Setter & Getter
    
    public SlotNumber getSlotNumber()
    {
        return slotNumber;
    }
    
    public void setSlotNumber(SlotNumber slotNumber)
    {
        this.slotNumber = slotNumber;
    }
    
    public ElementNumber getElementNumber()
    {
        return elementNumber;
    }
    
    public void setElementNumber(ElementNumber elementNumber)
    {
        this.elementNumber = elementNumber;
    }
}
