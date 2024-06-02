package main

import (
	"fmt"
	"gorm.io/datatypes"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"gorm.io/gorm/schema"
)

//go:generate stringer -type=Language
type Language int8

const (
	Unknown Language = iota
	French
	Dutch
	German
	English
)

/*
func (s Language) String() string {
	switch s {
	case Unknown:
		return "Unknown"
	case French:
		return "French"
	case Dutch:
		return "Dutch"
	case German:
		return "German"
	case English:
		return "English"
	}
	return "unknown"
}
*/

//go:generate stringer -type=TypeOfDenomination
type TypeOfDenomination int8

const (
	Name TypeOfDenomination = iota
	Abbreviation
	CommercialName
	BranchName
)

/*
func (s TypeOfDenomination) String() string {
	switch s {
	case Name:
		return "Name"
	case Abbreviation:
		return "Abbreviation"
	case CommercialName:
		return "CommercialName"
	case BranchName:
		return "BranchName"
	}
	return "unknown"
}
*/

type Denomination struct {
	ID                 int `gorm:"column:id"`
	EntityNumber       string
	Language           Language
	TypeOfDenomination TypeOfDenomination
	Denomination       string
}

type Address struct {
	ID               int `gorm:"column:id"`
	EntityNumber     string
	TypeOfAddress    string
	CountryNL        string
	CountryFR        string
	Zipcode          string
	MunicipalityNL   string
	MunicipalityFR   string
	StreetNL         string
	StreetFR         string
	HouseNumber      string
	Box              string
	ExtraAddressInfo string
	DateStrikingOff  *datatypes.Date
}

func main() {
	// refer https://github.com/go-sql-driver/mysql#dsn-data-source-name for details
	dsn := "root:@tcp(127.0.0.1:3306)/kbo?charset=utf8mb4&parseTime=True&loc=Local"
	db, _ := gorm.Open(mysql.Open(dsn), &gorm.Config{
		NamingStrategy: schema.NamingStrategy{
			SingularTable: true,
			NoLowerCase:   true,
		}})

	addr := Address{}
	db.First(&addr)
	fmt.Println(addr)

	var denominations []Denomination
	//db.Where("Denomination = ?", "VDAB").Find(&denominations)
	db.Where("Denomination = ? and TypeOfDenomination != ?", "VDAB", BranchName).Find(&denominations)
	for _, d := range denominations {
		fmt.Println(d)
	}

}

/*func determineBestName(denominations []Denomination) string {
	for _, denomination := range denominations {

	}
}

func determineOne(denominations []Denomination, dtype TypeOfDenomination) string {
	m := make(map[Language]Denomination)
	for _, denomination := range denominations {
		if denomination.TypeOfDenomination == dtype {
			m[denomination.Language] = denomination
		}
	}
	preferred_languages := []Language{Dutch, French, English, German, Unknown}
	var myset map[Denomination]struct{}
	itar

}
*/
