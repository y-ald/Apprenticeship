enum quality:
    case high, low

enum typeOfConnection:
    case mobileNetwork, fixedNetwork


case class video (quality: quality, durationInSeconde: Int, typeOfConection:typeOfConnection):
    val videoDefinition: Double = 
        quality match
            case high => 0.6
            case low => 0.3
    val netWorkConsumption: Double = 
        typeOfConection match
            case mobileNetwork => 0.00088
            case fixedNetwork => 0.00042
    val dataCenterConsumerPerMB = 0.000072
        
        
    def computeImpact(): Double =
        netWorkConsumption * dataCenterConsumerPerMB * videoDefinition * durationInSeconde