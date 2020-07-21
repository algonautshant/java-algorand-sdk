package indexer

import (
	"context"
	"encoding/base64"
	"time"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
	"github.com/algorand/go-algorand-sdk/types"
)

// SearchForTransactions /v2/transactions
// Search for transactions.
type SearchForTransactions struct {
	c *Client
	p models.SearchForTransactionsParams
}

// Address only include transactions with this address in one of the transaction
// fields.
func (s *SearchForTransactions) Address(address types.Address) *SearchForTransactions {
	s.p.Address = address.String()
	return s
}

// AddressRole combine with the address parameter to define what type of address to
// search for.
func (s *SearchForTransactions) AddressRole(addressRole string) *SearchForTransactions {
	s.p.AddressRole = addressRole
	return s
}

// AfterTime include results after the given time. Must be an RFC 3339 formatted
// string.
func (s *SearchForTransactions) AfterTime(afterTime time.Time) *SearchForTransactions {
	s.p.AfterTime = afterTime.Format(time.RFC3339)
	return s
}

// ApplicationId application ID
func (s *SearchForTransactions) ApplicationId(applicationId uint64) *SearchForTransactions {
	s.p.ApplicationId = applicationId
	return s
}

// AssetID asset ID
func (s *SearchForTransactions) AssetID(assetID uint64) *SearchForTransactions {
	s.p.AssetID = assetID
	return s
}

// BeforeTime include results before the given time. Must be an RFC 3339 formatted
// string.
func (s *SearchForTransactions) BeforeTime(beforeTime time.Time) *SearchForTransactions {
	s.p.BeforeTime = beforeTime.Format(time.RFC3339)
	return s
}

// CurrencyGreaterThan results should have an amount greater than this value.
// MicroAlgos are the default currency unless an asset-id is provided, in which
// case the asset will be used.
func (s *SearchForTransactions) CurrencyGreaterThan(currencyGreaterThan uint64) *SearchForTransactions {
	s.p.CurrencyGreaterThan = currencyGreaterThan
	return s
}

// CurrencyLessThan results should have an amount less than this value. MicroAlgos
// are the default currency unless an asset-id is provided, in which case the asset
// will be used.
func (s *SearchForTransactions) CurrencyLessThan(currencyLessThan uint64) *SearchForTransactions {
	s.p.CurrencyLessThan = currencyLessThan
	return s
}

// ExcludeCloseTo combine with address and address-role parameters to define what
// type of address to search for. The close to fields are normally treated as a
// receiver, if you would like to exclude them set this parameter to true.
func (s *SearchForTransactions) ExcludeCloseTo(excludeCloseTo bool) *SearchForTransactions {
	s.p.ExcludeCloseTo = excludeCloseTo
	return s
}

// Limit maximum number of results to return.
func (s *SearchForTransactions) Limit(limit uint64) *SearchForTransactions {
	s.p.Limit = limit
	return s
}

// MaxRound include results at or before the specified max-round.
func (s *SearchForTransactions) MaxRound(maxRound uint64) *SearchForTransactions {
	s.p.MaxRound = maxRound
	return s
}

// MinRound include results at or after the specified min-round.
func (s *SearchForTransactions) MinRound(minRound uint64) *SearchForTransactions {
	s.p.MinRound = minRound
	return s
}

// Next the next page of results. Use the next token provided by the previous
// results.
func (s *SearchForTransactions) Next(next string) *SearchForTransactions {
	s.p.Next = next
	return s
}

// NotePrefix specifies a prefix which must be contained in the note field.
func (s *SearchForTransactions) NotePrefix(notePrefix []byte) *SearchForTransactions {
	s.p.NotePrefix = base64.StdEncoding.EncodeToString(notePrefix)
	return s
}

// RekeyTo include results which include the rekey-to field.
func (s *SearchForTransactions) RekeyTo(rekeyTo bool) *SearchForTransactions {
	s.p.RekeyTo = rekeyTo
	return s
}

// Round include results for the specified round.
func (s *SearchForTransactions) Round(round uint64) *SearchForTransactions {
	s.p.Round = round
	return s
}

// SigType filters just results using the specified type of signature:
// * sig - Standard
// * msig - MultiSig
// * lsig - LogicSig
func (s *SearchForTransactions) SigType(sigType string) *SearchForTransactions {
	s.p.SigType = sigType
	return s
}

func (s *SearchForTransactions) TxType(txType string) *SearchForTransactions {
	s.p.TxType = txType
	return s
}

// TxID lookup the specific transaction by ID.
func (s *SearchForTransactions) TxID(txID string) *SearchForTransactions {
	s.p.TxID = txID
	return s
}

// Do performs HTTP request
func (s *SearchForTransactions) Do(ctx context.Context,
	headers ...*common.Header) (response models.TransactionsResponse, err error) {
	err = s.c.get(ctx, &response,
		"/v2/transactions", s.p, headers)
	return
}
