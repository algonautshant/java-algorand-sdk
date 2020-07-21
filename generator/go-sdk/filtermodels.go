package models

// GetPendingTransactionsByAddressParams defines parameters for
// GetPendingTransactionsByAddress
type GetPendingTransactionsByAddressParams struct {
	// Max truncated number of transactions to display. If max=0, returns all pending
	// txns.
	Max uint64 `url:"max,omitempty"`
}

// GetPendingTransactionsParams defines parameters for GetPendingTransactions
type GetPendingTransactionsParams struct {
	// Max truncated number of transactions to display. If max=0, returns all pending
	// txns.
	Max uint64 `url:"max,omitempty"`
}

// SearchForAccountsParams defines parameters for SearchForAccounts
type SearchForAccountsParams struct {
	// ApplicationId application ID
	ApplicationId uint64 `url:"application-id,omitempty"`

	// AssetID asset ID
	AssetID uint64 `url:"asset-id,omitempty"`

	// AuthAddr include accounts configured to use this spending key.
	AuthAddr string `url:"auth-addr,omitempty"`

	// CurrencyGreaterThan results should have an amount greater than this value.
	// MicroAlgos are the default currency unless an asset-id is provided, in which
	// case the asset will be used.
	CurrencyGreaterThan uint64 `url:"currency-greater-than,omitempty"`

	// CurrencyLessThan results should have an amount less than this value. MicroAlgos
	// are the default currency unless an asset-id is provided, in which case the asset
	// will be used.
	CurrencyLessThan uint64 `url:"currency-less-than,omitempty"`

	// Limit maximum number of results to return.
	Limit uint64 `url:"limit,omitempty"`

	// Next the next page of results. Use the next token provided by the previous
	// results.
	Next string `url:"next,omitempty"`

	// Round include results for the specified round. For performance reasons, this
	// parameter may be disabled on some configurations.
	Round uint64 `url:"round,omitempty"`
}

// LookupAccountByIDParams defines parameters for LookupAccountByID
type LookupAccountByIDParams struct {
	// Round include results for the specified round.
	Round uint64 `url:"round,omitempty"`
}

// LookupAccountTransactionsParams defines parameters for LookupAccountTransactions
type LookupAccountTransactionsParams struct {
	// AfterTime include results after the given time. Must be an RFC 3339 formatted
	// string.
	AfterTime string `url:"after-time,omitempty"`

	// AssetID asset ID
	AssetID uint64 `url:"asset-id,omitempty"`

	// BeforeTime include results before the given time. Must be an RFC 3339 formatted
	// string.
	BeforeTime string `url:"before-time,omitempty"`

	// CurrencyGreaterThan results should have an amount greater than this value.
	// MicroAlgos are the default currency unless an asset-id is provided, in which
	// case the asset will be used.
	CurrencyGreaterThan uint64 `url:"currency-greater-than,omitempty"`

	// CurrencyLessThan results should have an amount less than this value. MicroAlgos
	// are the default currency unless an asset-id is provided, in which case the asset
	// will be used.
	CurrencyLessThan uint64 `url:"currency-less-than,omitempty"`

	// Limit maximum number of results to return.
	Limit uint64 `url:"limit,omitempty"`

	// MaxRound include results at or before the specified max-round.
	MaxRound uint64 `url:"max-round,omitempty"`

	// MinRound include results at or after the specified min-round.
	MinRound uint64 `url:"min-round,omitempty"`

	// Next the next page of results. Use the next token provided by the previous
	// results.
	Next string `url:"next,omitempty"`

	// NotePrefix specifies a prefix which must be contained in the note field.
	NotePrefix []byte `url:"note-prefix,omitempty"`

	// RekeyTo include results which include the rekey-to field.
	RekeyTo bool `url:"rekey-to,omitempty"`

	// Round include results for the specified round.
	Round uint64 `url:"round,omitempty"`

	// SigType filters just results using the specified type of signature:
	// * sig - Standard
	// * msig - MultiSig
	// * lsig - LogicSig
	SigType string `url:"sig-type,omitempty"`

	TxType string `url:"tx-type,omitempty"`

	// TxID lookup the specific transaction by ID.
	TxID string `url:"txid,omitempty"`
}

// SearchForApplicationsParams defines parameters for SearchForApplications
type SearchForApplicationsParams struct {
	// ApplicationId application ID
	ApplicationId uint64 `url:"application-id,omitempty"`
}

// SearchForAssetsParams defines parameters for SearchForAssets
type SearchForAssetsParams struct {
	// AssetID asset ID
	AssetID uint64 `url:"asset-id,omitempty"`

	// Creator filter just assets with the given creator address.
	Creator string `url:"creator,omitempty"`

	// Limit maximum number of results to return.
	Limit uint64 `url:"limit,omitempty"`

	// Name filter just assets with the given name.
	Name string `url:"name,omitempty"`

	// Next the next page of results. Use the next token provided by the previous
	// results.
	Next string `url:"next,omitempty"`

	// Unit filter just assets with the given unit.
	Unit string `url:"unit,omitempty"`
}

// LookupAssetBalancesParams defines parameters for LookupAssetBalances
type LookupAssetBalancesParams struct {
	// CurrencyGreaterThan results should have an amount greater than this value.
	// MicroAlgos are the default currency unless an asset-id is provided, in which
	// case the asset will be used.
	CurrencyGreaterThan uint64 `url:"currency-greater-than,omitempty"`

	// CurrencyLessThan results should have an amount less than this value. MicroAlgos
	// are the default currency unless an asset-id is provided, in which case the asset
	// will be used.
	CurrencyLessThan uint64 `url:"currency-less-than,omitempty"`

	// Limit maximum number of results to return.
	Limit uint64 `url:"limit,omitempty"`

	// Next the next page of results. Use the next token provided by the previous
	// results.
	Next string `url:"next,omitempty"`

	// Round include results for the specified round.
	Round uint64 `url:"round,omitempty"`
}

// LookupAssetTransactionsParams defines parameters for LookupAssetTransactions
type LookupAssetTransactionsParams struct {
	// Address only include transactions with this address in one of the transaction
	// fields.
	Address string `url:"address,omitempty"`

	// AddressRole combine with the address parameter to define what type of address to
	// search for.
	AddressRole string `url:"address-role,omitempty"`

	// AfterTime include results after the given time. Must be an RFC 3339 formatted
	// string.
	AfterTime string `url:"after-time,omitempty"`

	// BeforeTime include results before the given time. Must be an RFC 3339 formatted
	// string.
	BeforeTime string `url:"before-time,omitempty"`

	// CurrencyGreaterThan results should have an amount greater than this value.
	// MicroAlgos are the default currency unless an asset-id is provided, in which
	// case the asset will be used.
	CurrencyGreaterThan uint64 `url:"currency-greater-than,omitempty"`

	// CurrencyLessThan results should have an amount less than this value. MicroAlgos
	// are the default currency unless an asset-id is provided, in which case the asset
	// will be used.
	CurrencyLessThan uint64 `url:"currency-less-than,omitempty"`

	// ExcludeCloseTo combine with address and address-role parameters to define what
	// type of address to search for. The close to fields are normally treated as a
	// receiver, if you would like to exclude them set this parameter to true.
	ExcludeCloseTo bool `url:"exclude-close-to,omitempty"`

	// Limit maximum number of results to return.
	Limit uint64 `url:"limit,omitempty"`

	// MaxRound include results at or before the specified max-round.
	MaxRound uint64 `url:"max-round,omitempty"`

	// MinRound include results at or after the specified min-round.
	MinRound uint64 `url:"min-round,omitempty"`

	// Next the next page of results. Use the next token provided by the previous
	// results.
	Next string `url:"next,omitempty"`

	// NotePrefix specifies a prefix which must be contained in the note field.
	NotePrefix []byte `url:"note-prefix,omitempty"`

	// RekeyTo include results which include the rekey-to field.
	RekeyTo bool `url:"rekey-to,omitempty"`

	// Round include results for the specified round.
	Round uint64 `url:"round,omitempty"`

	// SigType filters just results using the specified type of signature:
	// * sig - Standard
	// * msig - MultiSig
	// * lsig - LogicSig
	SigType string `url:"sig-type,omitempty"`

	TxType string `url:"tx-type,omitempty"`

	// TxID lookup the specific transaction by ID.
	TxID string `url:"txid,omitempty"`
}

// SearchForTransactionsParams defines parameters for SearchForTransactions
type SearchForTransactionsParams struct {
	// Address only include transactions with this address in one of the transaction
	// fields.
	Address string `url:"address,omitempty"`

	// AddressRole combine with the address parameter to define what type of address to
	// search for.
	AddressRole string `url:"address-role,omitempty"`

	// AfterTime include results after the given time. Must be an RFC 3339 formatted
	// string.
	AfterTime string `url:"after-time,omitempty"`

	// ApplicationId application ID
	ApplicationId uint64 `url:"application-id,omitempty"`

	// AssetID asset ID
	AssetID uint64 `url:"asset-id,omitempty"`

	// BeforeTime include results before the given time. Must be an RFC 3339 formatted
	// string.
	BeforeTime string `url:"before-time,omitempty"`

	// CurrencyGreaterThan results should have an amount greater than this value.
	// MicroAlgos are the default currency unless an asset-id is provided, in which
	// case the asset will be used.
	CurrencyGreaterThan uint64 `url:"currency-greater-than,omitempty"`

	// CurrencyLessThan results should have an amount less than this value. MicroAlgos
	// are the default currency unless an asset-id is provided, in which case the asset
	// will be used.
	CurrencyLessThan uint64 `url:"currency-less-than,omitempty"`

	// ExcludeCloseTo combine with address and address-role parameters to define what
	// type of address to search for. The close to fields are normally treated as a
	// receiver, if you would like to exclude them set this parameter to true.
	ExcludeCloseTo bool `url:"exclude-close-to,omitempty"`

	// Limit maximum number of results to return.
	Limit uint64 `url:"limit,omitempty"`

	// MaxRound include results at or before the specified max-round.
	MaxRound uint64 `url:"max-round,omitempty"`

	// MinRound include results at or after the specified min-round.
	MinRound uint64 `url:"min-round,omitempty"`

	// Next the next page of results. Use the next token provided by the previous
	// results.
	Next string `url:"next,omitempty"`

	// NotePrefix specifies a prefix which must be contained in the note field.
	NotePrefix []byte `url:"note-prefix,omitempty"`

	// RekeyTo include results which include the rekey-to field.
	RekeyTo bool `url:"rekey-to,omitempty"`

	// Round include results for the specified round.
	Round uint64 `url:"round,omitempty"`

	// SigType filters just results using the specified type of signature:
	// * sig - Standard
	// * msig - MultiSig
	// * lsig - LogicSig
	SigType string `url:"sig-type,omitempty"`

	TxType string `url:"tx-type,omitempty"`

	// TxID lookup the specific transaction by ID.
	TxID string `url:"txid,omitempty"`
}
